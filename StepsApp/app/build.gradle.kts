plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.stepsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.stepsapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.wear)
    implementation(libs.play.services.wearable)
    implementation(libs.healthservices)
    implementation ("com.google.android.gms:play-services-tasks:18.3.0")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.1.3")
}
