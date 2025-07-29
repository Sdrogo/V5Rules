# File: app/proguard-rules.pro

# Regole specifiche per le librerie di Firebase
# Risolve: Missing class com.google.firebase.perf.network.FirebasePerfUrlConnection
-keep class com.google.firebase.perf.network.** { *; }
-keep class com.google.android.recaptcha.** { *; }
-keep class com.google.firebase.appcheck.** { *; }

# Regole generali per Firebase, Google & Play Services
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.firebase.** { *; }
-keepattributes Signature, InnerClasses

# Regole per classi di sistema referenziate da dipendenze comuni
# Risolve: sun.misc.Unsafe, javax.naming.*
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