
# 📦 Courier Tracking API (Spring Boot)

A geolocation-based courier tracking RESTful API built with Java 17 and Spring Boot. This application streams courier locations, tracks entries near stores, and calculates total travel distances.

---

## 🚀 Features

- 📍 Accepts real-time courier location data
- 📊 Calculates total travel distance per courier
- 🏪 Detects when couriers enter within 100 meters of Migros stores
- ⏱ Prevents duplicate store entries within 1 minute
- 📁 Reads static store data from `stores.json`
- 🧪 Includes unit tests for controller, service, and strategy classes
- 🧩 Implements Strategy and Singleton design patterns

---

## 🧰 Technologies

- Java 17
- Spring Boot 3
- Spring Data JPA (H2 Database)
- Lombok
- JUnit 5 & Mockito
- Gradle

---

## ⚙️ How to Run

### 1. Clone the repo
```bash
git clone https://github.com/mertdestici/CourierTracking.git
cd courier-tracking
```

### 2. Run the application

### ✅ Option 1: Run via Gradle
```bash
./gradlew bootRun
```

### 🐳 Option 2: Run via Docker (Optional)
```bash
./gradlew build
docker build -t courier-tracking-api .
docker run -p 8080:8080 courier-tracking-api
```

### 3. Test endpoints using Postman or curl
[Courier Tracking API.postman_collection.json](Courier%20Tracking%20API.postman_collection.json)

#### POST Location
```http
POST /api/locations
Content-Type: application/json

{
  "courierId": "courier_001",
  "latitude": 41.0,
  "longitude": 29.0,
  "timestamp": "2025-04-20T12:00:00"
}
```

#### GET Total Distance
```http
GET /api/locations/distance/courier_001
```

#### GET Nearby Couriers
```http
GET /api/locations/nearby/Migros Beşiktaş
```

---

## 🧪 Running Tests

```bash
./gradlew test
```

