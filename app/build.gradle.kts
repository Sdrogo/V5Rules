plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    }

android {
    namespace = "com.example.v5rules"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.v5rules"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom)) // Importa il BOM
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.room.compiler) // Usa ksp, non kapt
    implementation(libs.androidx.room.runtime)
    implementation(libs.coil.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler) // Usa ksp, non kapt
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling) //debugImplementation per evitare di includerlo nella release
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.material3) // NESSUNA VERSIONE, il BOM gestisce la versione!
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.com.google.code.gson)
    implementation(libs.room.ktx)
    implementation(libs.serialization)
    implementation (libs.kotlinx.coroutines.core)
    implementation(libs.androidx.constraintlayout) // constraintlayout per View System
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.room.runtime) // o la versione più recente
    ksp(libs.androidx.room.compiler) // o la versione più recente
    implementation(libs.room.ktx)
    ksp(libs.hilt.android.compiler)

}