# File: app/proguard-rules.pro

# Regole per Firebase Performance Monitoring e le sue dipendenze (Recaptcha)
# Questo risolve l'errore "Missing class com.google.firebase.perf.network.FirebasePerfUrlConnection"
-keep class com.google.firebase.perf.network.** { *; }
-keep class com.google.android.recaptcha.** { *; }
-keepattributes Signature, InnerClasses

# Regola per sun.misc.Unsafe
# Un errore comune quando si usano librerie come Coroutines o OkHttp
-keep class sun.misc.Unsafe { *; }

# Regola per le classi Javax Naming
# Risolve gli errori "javax.naming.*"
-keep class javax.naming.** { *; }
-keep class javax.security.** { *; }

# Regole generali per Firebase e Google Play Services
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.firebase.** { *; }

# Regole per Kotlinx Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.flow.internal.ChannelFlow { *; }
-keepnames class kotlinx.coroutines.flow.internal.CombineKt { *; }
-keepnames class kotlinx.coroutines.selects.SelectBuilder { *; }

# Regole per Kotlinx Serialization (che stai usando)
-keepattributes *Annotation*
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable <methods>;
}
-keep class **$$serializer { *; }

# Regole per Google GSON (che stai usando)
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Aggiungi anche questa regola se usi Hilt con Compose
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    @com.google.dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}