plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.arash.altafi.mvisample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.arash.altafi.mvisample"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://arashaltafi.ir/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = false // create apk for all cpu
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Gson
    implementation(libs.gson)

    // PersianDate
    implementation(libs.persiandate)

    // WorkManager Kotlin + coroutines //don't update !!!
    implementation(libs.androidx.work.runtime.ktx)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.android.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Lottie
    implementation(libs.lottie.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.common.ktx)

    // Tink for encryption
    implementation(libs.tink.android)

    // RX
    implementation(libs.rxandroid)

    // MultiDex
    implementation(libs.androidx.multidex)

    // Orbit MVI
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.orbit.mvi.orbit.compose)

    // PersianDate
    implementation(libs.persiandate)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Tests
    testImplementation(libs.orbit.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended.android)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
}