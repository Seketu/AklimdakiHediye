plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
    //hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.aklimdakihediye"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aklimdakihediye"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_KEY","\"AIzaSyAxp5b7hIyY8Yzb93QHFPFY8vFOK69X3GA\"")
        buildConfigField("String", "BASE_URL","\"https://generativelanguage.googleapis.com/\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    //lottie Animations dependicies
    implementation(libs.lottie.compose)

    //Okhttp dependicies
    implementation(libs.okhttp)

    // ktor
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.ktor.client.content.negotiation)
    implementation(libs.ktor.ktor.serialization.kotlinx.json)
    implementation (libs.ktor.client.plugins)
    implementation (libs.ktor.client.cio)

    //Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.rxjava3)
    implementation (libs.androidx.room.ktx)

    //jsoup
    implementation("org.jsoup:jsoup:1.15.4")
    //set theme
    implementation("androidx.appcompat:appcompat:1.6.1")
    //coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //ads
    implementation("com.google.android.gms:play-services-ads:24.2.0")

    implementation ("com.google.accompanist:accompanist-pager:0.34.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.34.0")


    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
kapt {
    correctErrorTypes = true
}