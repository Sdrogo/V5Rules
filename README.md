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


## 🤖 Automazione e CI/CD (Continuous Integration & Deployment)

Questo progetto utilizza GitHub Actions per automatizzare i processi di build, analisi, versioning e release. L'obiettivo è garantire la qualità del codice e creare build di release firmate e pronte per la distribuzione in modo affidabile e consistente.

Abbiamo configurato due workflow principali che lavorano in sinergia:

---

### 1. `android-ci.yml` - Build, Analisi e Release

Questo è il workflow principale che si occupa di compilare e analizzare l'applicazione.

**Trigger:**
* Su ogni `push` ai branch `master`, `feature/*`, `bugfix/*`.
* Su ogni `pull_request` verso i branch sopra elencati.
* Quando viene creato un nuovo **tag** (es. `v1.0.1`).

**Funzionamento:**
Il workflow è diviso in due job principali:

#### Job 1: `analyze` (Analisi del Codice)
Questo job viene eseguito su **ogni push e pull request**.
* Configura l'ambiente di build con JDK 17.
* Crea il file `google-services.json` a partire dai secret.
* Esegue un build di **debug** (`./gradlew assembleDebug`), che è sufficiente per l'analisi.
* Esegue un'analisi statica del codice per trovare potenziali bug e vulnerabilità di sicurezza usando **CodeQL**.
* **Scopo:** Fornire un feedback rapido sulla qualità e la correttezza del codice senza accedere a secret sensibili come le chiavi di firma.

#### Job 2: `build-and-release` (Creazione dell'APK Firmato)
Questo job viene eseguito **solo in due casi**:
1.  Dopo un merge (push) sul branch `master`.
2.  Quando viene fatto il push di un nuovo tag di versione.

* Dipende dal successo del job `analyze`.
* Configura l'ambiente e crea i file `google-services.json` e `keystore.jks` dai secret.
* **Versioning Automatico:**
    * **`versionCode`**: Calcolato automaticamente come il numero totale di commit nel repository.
    * **`versionName`**: Impostato in base al tag Git (es. `v1.0.1`) o come versione di sviluppo se il trigger è un push su `master` (es. `master-build-abcdef`).
* **Build e Firma:** Compila l'APK in modalità **release**, lo firma usando i secret e lo nomina dinamicamente (es. `V5Rules-v1.0.1.apk`).
* **Upload dell'Artefatto:** Carica l'APK firmato come artefatto del workflow, pronto per essere scaricato e distribuito.

---

### 2. `tag-release.yml` - Versioning Automatico

Questo workflow si occupa esclusivamente di **automatizzare la creazione dei tag di versione**, che a loro volta attivano il processo di release.

**Trigger:**
* **Automatico**: Su ogni push verso il branch `master`.
* **Manuale**: Può essere attivato manualmente dalla tab "Actions" di GitHub.

**Funzionamento:**

#### Modalità Automatica (`push` su `master`)
* Quando una Pull Request viene mergiata, questo workflow si attiva.
* Legge l'ultimo tag esistente e aumenta automaticamente la versione di **patch** (es. da `v1.2.3` a `v1.2.4`).
* Crea e fa il push del nuovo tag.

#### Modalità Manuale (`workflow_dispatch`)
Per le release più importanti (nuove funzionalità o cambiamenti radicali), puoi attivare il workflow manualmente:
1.  Vai alla tab **Actions** del repository.
2.  Seleziona il workflow **"Create and Push Tag"**.
3.  Clicca su **"Run workflow"**.
4.  Scegli dal menu a tendina se vuoi un aggiornamento `major` (es. `v2.0.0`), `minor` (es. `v1.3.0`) o `patch` (es. `v1.2.4`).
5.  Avvia il workflow, che creerà e pubblicherà il tag corretto.

Una volta che il nuovo tag viene creato, il workflow `android-ci.yml` si attiva per costruire la release finale.

Questo progetto è rilasciato sotto la Licenza [MIT](LICENSE). Sentiti libero di forkare, modificare e contribuire!

---
_Questa è un'applicazione non ufficiale creata da un fan e non è affiliata in alcun modo con Paradox Interactive o World of Darkness._
