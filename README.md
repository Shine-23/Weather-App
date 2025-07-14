# The Weather App

A full-stack application to search cities and track real-time weather updates via WebSocket.
- Frontend: React.js + Vite
- Backend: Spring Boot with REST API & WebSocket/STOMP.
- External API: https://openweathermap.org

## Features
- Search Weather by City: Current weather fetched on search.
- Real-Time Weather Updates: Automatic updates pushed every 30 seconds.
- Multiple City Tracking: Subscribe to multiple cities simultaneously.
- Remove/Unsubscribe Cities: Stop tracking a city.
- Session-Based Tracking:Tracks subscriptions per WebSocket session

## Prerequisites
- Java 17+
- Node.js
- Gradle

## Setup Instructions
### Backend Setup 
1. Update application.properties with your OpenWeatherMap API keys:
```bash
weather.api.key=YOUR_WEATHER_API_KEY
weather.api.base-url=https://api.openweathermap.org
```
2. Run the backend:
```bash
./mvnw spring-boot:run
```
3. Backend runs on: `http://localhost:8080`

###  Frontend Setup 
1. Install dependencies:
```bash
npm install
```
2. Start the development server:
```bash
npm run dev
```
3. Frontend runs on: `http://localhost:5173`
  

