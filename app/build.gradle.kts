import com.android.build.api.dsl.ApplicationExtension
import org.gradle.kotlin.dsl.dependencies
import java.io.FileInputStream
import java.util.Properties

fun getVersionCode(): Int {
    return if (project.hasProperty("appVersionCode")) {
        project.property("appVersionCode").toString().toInt()
    } else {
        1
    }
}

fun getVersionName(): String {
    return if (project.hasProperty("appVersionName")) {
        project.property("appVersionName").toString()
    } else {
        "1.0-LOCAL"
    }
}

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.firebase.firebase.perf)
}

configure<ApplicationExtension>  {
    namespace = "com.example.v5rules"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.v5rules"
        minSdk = 25
        targetSdk = 36
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            val keystoreFile = rootProject.file("my-release-key.keystore")

            if (keystorePropertiesFile.exists() && keystoreFile.exists()) {
                val properties = Properties()
                properties.load(FileInputStream(keystorePropertiesFile))
                storeFile = keystoreFile
                storePassword = properties.getProperty("storePassword")
                keyAlias = properties.getProperty("keyAlias")
                keyPassword = properties.getProperty("keyPassword")
            } else {
                val ciKeystoreFile = rootProject.file("keystore.jks")
                if (ciKeystoreFile.exists()) {
                    storeFile = ciKeystoreFile
                    storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                    keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                    keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        variant.outputs.forEach { output ->
            (output as com.android.build.api.variant.impl.VariantOutputImpl).outputFileName.set("V5Rules-${output.versionName.get()}.apk")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    implementation(libs.coil.compose)
    implementation(libs.hilt.android)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material.icons.extended)
    ksp(libs.hilt.android.compiler)

    // UNIT TEST (Logica)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    // UI TEST (Jetpack Compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.com.google.code.gson)
    implementation(libs.serialization)
    implementation (libs.kotlinx.coroutines.core)
    implementation(libs.androidx.constraintlayout.compose)
}
