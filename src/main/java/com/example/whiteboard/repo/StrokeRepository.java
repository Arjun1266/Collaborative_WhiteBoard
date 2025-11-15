package com.example.whiteboard.repo;

import com.example.whiteboard.model.Stroke;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrokeRepository extends MongoRepository<Stroke, String> {

    List<Stroke> findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
