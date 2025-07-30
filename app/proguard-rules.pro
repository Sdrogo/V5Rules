# File: app/proguard-rules.pro

# --- Regole per Firebase e Google Play Services ---

# Mantiene i nomi delle classi di dati per Firestore, Auth, etc.
-keepnames class com.example.v5rules.data.**

# Mantiene i campi annotati con @PropertyName nelle classi di dati
-keepattributes Signature
-keepclassmembers class com.example.v5rules.data.** {
    @com.google.firebase.firestore.PropertyName <fields>;
}

# Regole generali per le librerie Firebase e Google
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.perf.network.** { *; }
-keep class com.google.android.recaptcha.** { *; }
-keep class com.google.firebase.appcheck.** { *; }


# --- Regole per Librerie Essenziali ---

# Mantiene classi di sistema usate da librerie come OkHttp o Coroutines
# noinspection ShrinkerUnresolvedReference
-keep class sun.misc.Unsafe { *; }
-keep class javax.naming.** { *; }
-keep class javax.security.** { *; }

# Regole per Kotlinx Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.flow.internal.ChannelFlow { *; }
-keepnames class kotlinx.coroutines.flow.internal.CombineKt { *; }

# Regole per Kotlinx Serialization
-keepattributes *Annotation*
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable <methods>;
}
-keep class **$$serializer { *; }

# Regole per Google GSON
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Regole per Hilt e ViewModel
# noinspection ShrinkerUnresolvedReference
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    @com.google.dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}