package com.example.whiteboard.controller;

import com.example.whiteboard.model.User;
import com.example.whiteboard.repo.UserRepository;
import com.example.whiteboard.security.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (repo.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("USER_EXISTS");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return ResponseEntity.ok("SIGNUP_SUCCESS");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = repo.findByUsername(req.getUsername());

        if (user == null)
            return ResponseEntity.status(401).body("INVALID_USER");

        if (!encoder.matches(req.getPassword(), user.getPassword()))
            return ResponseEntity.status(401).body("INVALID_PASSWORD");

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

