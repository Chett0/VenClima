# VenClima
<div align="center">
  <img src="frontend/app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" alt="VenClima Logo" width="200">
</div>

VenClima è un'applicazione che consente di monitorare in tempo reale l’andamento delle maree nella città di Venezia.

Di seguito viene fornita una guida rapida per l’installazione e l’avvio dell’app, sia su emulatore che su dispositivo fisico.

## 🛠️ Prerequisiti

- Java: JDK 11+ (consigliato JDK 17)

- Android Studio: con SDK e emulatore installati

- Maven 3.8+ (per il backend)

- Git

- IDE consigliato: IntelliJ IDEA (per il backend)

- Database: PostgreSQL 

### 📂 Clonazione del repository

```bash
    git clone https://github.com/Chett0/VenClima.git
    cd VenClima
```

## 💻 Backend Spring Boot

### ⚙️ Configurazione dell'applicazione
Configura i parametri principali in base all'ambiente
#### File `application.properties`


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
---

### 📦 Installazione delle dipendenze

Con Maven:

```bash
mvn clean install
```
---


### 🚀 Avvio dell'applicazione

#### Da terminale

```bash
mvn spring-boot:run
```

#### Da IDE

- Importa il progetto come **Maven project**
- Avvia la classe principale **BackendApplication**
- 
---

### 🧪 Test

Esegui i test automatici con:

```bash
mvn test
```

---


### 🗂️ Struttura del backend
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

## 📱 Frontend Android
### ⚙️ Configurazione dell'applicazione
Configura i parametri principali in base all'ambiente
#### File `local.properties`
```properties
sdk.dir=/Users/username/Library/Android/sdk
API_BASE_URL=http://api.example.com
```

---

### 🚀 Apertura del progetto

1. Apri Android Studio
2. Seleziona Open
3. Scegli la cartella del progetto clonata
4. Attendi il completamento della Gradle Sync

---

### 🏗️ Build del progetto

Da Android Studio:
- Build > Make Project

---

### 📱 Esecuzione dell'app

**Emulatore**

1. Apri Device Manager
2. Avvia un emulatore
3. Premi Run ▶️

**Dispositivo fisico**

1. Abilita Opzioni sviluppatore
2. Attiva USB Debugging
3. Collega il dispositivo via USB
4. Premi Run ▶️

---

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

## 🔥 Configurazione Firebase Cloud Messaging

1. Vai su Firebase Console

2. Crea un nuovo progetto (o usa uno esistente)

3. Abilita Cloud Messaging nelle impostazioni del progetto

4. Scarica i file chiave:

- 🗝️ Backend (Server key JSON): serviceAccountKey.json

- 🤖 Frontend Android: google-services.json
