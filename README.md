# VenClima
<div align="center">
  <img src="frontend/app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" alt="VenClima Logo" width="200">
</div>

VenClima is an application designed to monitor real-time tide levels in the city of Venice.

This guide provides a quick overview of how to install and run the application, both on an emulator and on a physical device.

## 🛠️ Prerequisites

Make sure you have the following installed:

- Java: JDK 11+

- Android Studio: with SDK and emulator configured

- Maven 3.8+ 

- Database: PostgreSQL 

### 📂 Clone the Repository

```bash
    git clone https://github.com/Chett0/VenClima.git
    cd VenClima
```

---

## 💻 Backend Setup (Spring Boot)

### ⚙️ Configuration
Edit `application.properties`


```properties
spring.application.name=backend

spring.datasource.url=jdbc:postgresql://localhost:5432/nome_db
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

security.jwt.secret-key=secret_key
security.jwt.expiration-time=3600000

logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.http.converter.json=DEBUG

server.port=8080
```

### 📦 Install Dependencies

```bash
mvn clean install
```


### 🚀 Run the Backend

#### From terminal

```bash
mvn spring-boot:run
```

#### From IDE

- Import as a **Maven project**
- Run **BackendApplication**

### 🧪 Testing

```bash
mvn test
```



### 🗂️ Backend Structure
```
src/
 └── main/
     ├── java/com/example/venclima
     │   ├── config
     │   ├── controller
     │   ├── dto
     │   ├── mapper
     │   ├── model
     │   ├── response
     │   ├── service
     │   ├── repository
     │   ├── utils
     │   └── BackendApplication.java
     └── resources
         ├── application.properties
         ├── firebase.json
         ├── Veniceislands.json
 └── test/
     ├── java/com/example/venclima
         ├── controller
         ├── service
         └── BackendApplication.java
```

---

## 📱 Frontend Setup (Android)
### ⚙️ Configuration
Edit `local.properties`
```properties
sdk.dir=/Users/username/Library/Android/sdk
API_BASE_URL=http://api.example.com
```


### 🏗️ Open & Build

- Open the Project in Android Studio
- Wait for Gradle Sync
- Build the project
    - Build > Make Project


### 📱 Run the App

**Emulator**

1. Open Device Manager
2. Start an emulator
3. Click Run ▶️

**Physical Device**

1. Enable Developer Options
2. Turn on USB Debugging
3. Connect your device via USB
4. Click Run ▶️


### 📂 Struttura del frontend

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/venclima
│   │   │   ├── adapters
│   │   │   ├── models
│   │   │   ├── network
│   │   │   ├── notifications
│   │   │   ├── utils
│   │   │   ├── viewModels
│   │   │   ├── views
│   │   │   └── VenClimaApp
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   ├── test/
│   └── androidTest/
├── build.gradle
├── local.properties
└── google-service.json
```

---

## 🔥 Firebase Cloud Messaging Setup

1. Go to Firebase Console

2. Create or select a project
3. Enable Cloud Messaging

4. Download and place:

- 🗝️ Backend: `serviceAccountKey.json`

- 🤖 Android: `google-services.json`
