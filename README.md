**VenClima — Istruzioni di installazione e avvio**

Breve guida per installare e avviare l'app (backend Java + frontend Android) e per eseguirla su un emulatore.

*Prerequisiti**
- **Java**: JDK 11+ (consigliato JDK 17), per backend.
- **Android Studio**: con SDK e emulatore installati, per frontend.

**Per avviare il backend**
Per avviarlo bisogna impostare l'indirizzo della macchina sulla quale si vuole farlo partire, nel file application.properties (server.address=0.0.0.0 permette a dispostivi/emulatori di raggiungere il server).
Sincronizzare le librerie necessarie tramite Maven.
Nel file application.properties impostare anche la stringa di connessione al database correttamente (es: spring.datasource.url=jdbc:postgresql://localhost:5432/VenClima)

**Per avviare il frontend**
Per avviare bisogna aprire la cartella `frontend` in Android Studio e lasciare che Gradle sincronizzi.
Dopodiché avviare un Android Virtual Device (emulatore), o collegare un dispositivo Android al PC. 
Dopo la sincronizzazione dei file, per avviare effettivamente l'applicazione premere il tasto start presente su Android Studio.
Inoltre deve essere impostato il corretto indirizzo del server, nel file local.properties (http://10.0.2.2:8080/ per emulatore).
