# V5Rules - Companion App per Vampiri: La Masquerade V5

![Logo V5](app/src/main/res/drawable/logo_v5.png)

Benvenuto in **V5Rules**, un'applicazione Android non ufficiale progettata per essere il compagno digitale definitivo per i giocatori e i narratori di **Vampiri: La Masquerade 5a Edizione**. L'app mira a centralizzare la creazione dei personaggi, la gestione delle schede e la rapida consultazione delle regole, tutto in un'unica interfaccia moderna e intuitiva.

L'applicazione è sviluppata interamente in italiano per supportare la comunità di giocatori italiana.

## 🚀 Funzionalità Principali

* **Scheda del Personaggio Digitale:** Crea, modifica e gestisci i tuoi personaggi con una scheda completa e interattiva che tiene traccia di:
    * Informazioni generali (nome, clan, predatore, etc.).
    * Attributi e Abilità.
    * Discipline, Poteri e Rituali.
    * Vantaggi (Background, Pregi, Difetti).
    * Salute, Forza di Volontà e Fame.
* **Sincronizzazione su Cloud:** Grazie all'integrazione con **Firebase Firestore**, le tue schede personaggio sono salvate online e sincronizzate sul tuo account Google, accessibili da qualsiasi dispositivo Android.
* **Compendio delle Regole:** Un vasto database interno per consultare rapidamente:
    * Descrizioni dettagliate dei Clan.
    * Elenco completo di Discipline, Poteri e Rituali.
    * Loresheet, Background e regole generali del gioco.
* **Generatore di PNG:** Un pratico strumento per generare al volo Personaggi Non Giocanti (PNG) con nomi casuali basati su diverse nazionalità, con la possibilità di salvare i preferiti.
* **Autenticazione Semplice:** Accesso rapido e sicuro tramite il tuo account Google.

## 🛠️ Stack Tecnologico

Questo progetto è costruito utilizzando le tecnologie e le best practice più moderne per lo sviluppo Android:

* **Linguaggio:** 100% **Kotlin**.
* **Interfaccia Utente:** **Jetpack Compose**, il moderno toolkit dichiarativo di Google per la UI.
* **Architettura:** **MVVM** (Model-View-ViewModel) per una chiara separazione dei compiti e un'alta manutenibilità.
* **Asynchronous Programming:** **Kotlin Coroutines** e **Flow** per la gestione delle operazioni asincrone e dei flussi di dati.
* **Dependency Injection:** **Hilt** per gestire le dipendenze in modo pulito e scalabile.
* **Database Remoto:** **Firebase Firestore** per l'archiviazione dei dati degli utenti.
* **Autenticazione:** **Firebase Authentication** (Google Sign-In).
* **Navigazione:** **Jetpack Navigation for Compose** per la gestione della navigazione tra le schermate.
* **Serializzazione:** **Kotlinx Serialization** per il parsing dei dati JSON locali.

## 📦 Installazione e Setup

Per compilare ed eseguire il progetto localmente, segui questi passaggi:

1.  **Clona il repository:**
    ```bash
    git clone [https://github.com/tuo-username/v5rules.git](https://github.com/tuo-username/v5rules.git)
    ```
2.  **Apri in Android Studio:** Apri il progetto con una versione recente di Android Studio (Iguana o successiva consigliata).
3.  **Configura Firebase:**
    * Crea un nuovo progetto sulla [Firebase Console](https://console.firebase.google.com/).
    * Aggiungi un'app Android al progetto Firebase con il package name `com.example.v5rules`.
    * Abilita i servizi **Authentication** (con il provider Google) e **Firestore Database**.
    * Scarica il file `google-services.json` dal tuo progetto Firebase e inseriscilo nella cartella `app/`.
4.  **Compila ed Esegui:** Sincronizza il progetto con i file Gradle e avvialo su un emulatore o un dispositivo fisico.

## 📄 Fonti dei Dati

Tutte le regole, descrizioni e dati di gioco sono stati digitalizzati e sono archiviati in formato JSON all'interno della cartella `app/src/main/res/raw/`. Questi dati sono basati sul manuale di Vampiri: La Masquerade 5a Edizione.

## 📜 Licenza

Questo progetto è rilasciato sotto la Licenza [MIT](LICENSE). Sentiti libero di forkare, modificare e contribuire!

---
_Questa è un'applicazione non ufficiale creata da un fan e non è affiliata in alcun modo con Paradox Interactive o World of Darkness._
