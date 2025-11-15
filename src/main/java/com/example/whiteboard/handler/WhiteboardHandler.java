package com.example.whiteboard.handler;

import com.example.whiteboard.model.Stroke;
import com.example.whiteboard.repo.StrokeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WhiteboardHandler extends TextWebSocketHandler {

    private final StrokeRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Autowired
    public WhiteboardHandler(StrokeRepository repo) {
        this.repo = repo;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String roomId = getRoomFromURI(session);
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);

        String username = (String) session.getAttributes().get("username");

        // Send history
        List<Stroke> strokes = repo.findByRoomId(roomId);
        for (Stroke s : strokes) {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(s.getData())));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Map<String, Object> data = mapper.readValue(message.getPayload(), Map.class);
        String roomId = (String) data.getOrDefault("roomId", "default");

        String username = (String) session.getAttributes().get("username");

        // Clear
        if (Boolean.TRUE.equals(data.get("clear"))) {
            repo.deleteByRoomId(roomId);
            broadcastToRoom(roomId, Map.of("clear", true));
            return;
        }

        // Undo
        if (Boolean.TRUE.equals(data.get("undo"))) {
            List<Stroke> strokes = repo.findByRoomId(roomId);

            // Undo only USER’s last stroke
            Collections.reverse(strokes);
            for (Stroke s : strokes) {
                if (s.getUser().equals(username)) {
                    repo.deleteById(s.getId());
                    broadcastToRoom(roomId, Map.of("undo", true, "strokeId", s.getId()));
                    break;
                }
            }
            return;
        }

        // Normal Stroke
        String strokeId = UUID.randomUUID().toString();
        data.put("strokeId", strokeId);

        Stroke stroke = new Stroke(strokeId, roomId, username, data);
        repo.save(stroke);

        broadcastToRoom(roomId, data);
    }

    private void broadcastToRoom(String roomId, Map<String, Object> msg) {
        try {
            String json = mapper.writeValueAsString(msg);

            for (WebSocketSession s : rooms.getOrDefault(roomId, Set.of())) {
                if (s.isOpen())
                    s.sendMessage(new TextMessage(json));
            }
        } catch (Exception ignored) {}
    }

    private String getRoomFromURI(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return "default";
        String query = uri.getQuery();
        if (query == null) return "default";

        for (String part : query.split("&"))
            if (part.startsWith("room="))
                return part.substring(5);

        return "default";
    }
}


