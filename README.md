 # 📝 Collaborative Whiteboard – Spring Boot | WebSockets | JWT | MongoDB

A real-time collaborative whiteboard built using **Spring Boot**, **WebSockets**, **JWT authentication**, and **MongoDB**.
Multiple users can draw together with **live sync**, **color & brush size selection**, **undo**, and **clear** features.

---

## 🚀 Features

- 🔐 JWT Authentication (Signup + Login)
- 🌐 Real-time drawing via WebSockets
- 🎨 Color picker & brush size control
- ↩ Undo last stroke (per user)
- 🧽 Clear entire board for everyone
- 🗄 MongoDB stroke storage (history replay)
- 👥 Multiple rooms (`?room=class1`)
- 📱 Touch + mobile support

---

## 🧩 Tech Stack

**Backend**
- Spring Boot 3  
- Spring WebSocket  
- Spring Security  
- JWT 0.11.5  
- MongoDB  
- Lombok  

**Frontend**
- HTML5 Canvas  
- Vanilla JavaScript  
- WebSocket API  

---

## 📁 Project Structure

```
src/main/java/com/example/whiteboard
│
├── config/
│   ├── SecurityConfig.java
│   └── WebSocketConfig.java
│
├── controller/
│   └── AuthController.java
│
├── handler/
│   └── WhiteboardHandler.java
│
├── model/
│   ├── User.java
│   └── Stroke.java
│
├── repo/
│   ├── UserRepository.java
│   └── StrokeRepository.java
│
└── security/
    ├── JwtUtil.java
    └── WebSocketAuthHandshake.java
```

Frontend:

```
src/main/resources/static/
├── login.html
└── index.html
```

---

## ⚙️ How to Run

### 1️⃣ Start MongoDB
```
mongodb://localhost:27017
```

### 2️⃣ Start Application
```
mvn spring-boot:run
```

### 3️⃣ Open Login Page
```
http://localhost:8080/login.html
```

### 4️⃣ Login or Signup  
JWT token is stored in browser automatically.

### 5️⃣ Open Any Room  
```
http://localhost:8080/index.html?room=test
```

---

## 🎨 Whiteboard Tools

- Real-time drawing  
- Color picker  
- Brush size slider  
- Undo (per-user)  
- Clear entire board  
- Auto-resizing canvas  
- Smooth, fast drawing  

---

## 🔮 Future Enhancements

- Eraser tool  
- Export whiteboard as PNG  
- Live chat panel  
- User presence indicators  
- Classroom dashboard  

---

## 📄 License  
Open-source for learning & personal development.
