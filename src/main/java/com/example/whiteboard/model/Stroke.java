package com.example.whiteboard.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "strokes")
public class Stroke {
    @Id
    private String id;

    private String roomId;
    private String user; // optional
    private Map<String, Object> data;
}
