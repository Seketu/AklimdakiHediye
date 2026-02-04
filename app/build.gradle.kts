import java.util.Properties
import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    //Ksp Core Kotlin
    id("com.google.devtools.ksp")
    //hilt
    id("com.google.dagger.hilt.android")
}


val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { stream -> load(stream) }
    }
}
configure<ApplicationExtension> {
    namespace = "com.reylortechnology.aklimdakihediye"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.reylortechnology.aklimdakihediye"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "GEMINI_API_KEY", "\"${localProperties.getProperty("GEMINI_API_KEY")}\"")
        buildConfigField("String", "BASE_URL", "\"${localProperties.getProperty("BASE_URL")}\"")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit.jupiter)

    //Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.rxjava3)
    implementation (libs.androidx.room.ktx)

    //jsoup
    implementation(libs.jsoup)
    //set theme
    implementation(libs.androidx.appcompat)
    //coil
    implementation(libs.coil.compose)

    //ads
    implementation(libs.play.services.ads)

    implementation (libs.accompanist.pager)
    implementation (libs.accompanist.pager.indicators)

    //google fonts
    implementation (libs.androidx.ui.text.google.fonts)

    //icons
    implementation(libs.androidx.compose.material.icons.extended)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.auto.common)

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

    // Force consistent version for concurrent-futures across app and tests
    implementation(libs.androidx.concurrent.futures)
    androidTestImplementation(libs.androidx.concurrent.futures)
    androidTestImplementation(libs.androidx.concurrent.futures.ktx)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}