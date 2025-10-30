import org.gradle.kotlin.dsl.commonMain
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)


}



sqldelight {
    databases {
        create("InitiationDatabase") {
            packageName.set("com.example.taofamily.db")
            srcDirs.setFrom("src/commonMain/sqldelight")
        }
    }
}



kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")

            // 1. Foundation
            linkerOpts.add("-framework")
            linkerOpts.add("Foundation")

            // 2. Security
            linkerOpts.add("-framework")
            linkerOpts.add("Security")

            // 3. CoreFoundation
            linkerOpts.add("-framework")
            linkerOpts.add("CoreFoundation")
        }


    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp) // Ktor HTTP Client Engine for Android
//            implementation(libs.ktor.client.android) // Ktor HTTP Client Engine for Android
            implementation(libs.koin.android)       // Koin integration for Android context
            implementation(libs.sqldelight.driver.android) // Android driver

        }
        commonMain.dependencies {
            implementation(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // --- ADDED CORE SHARED DEPENDENCIES (Koin, Ktor, Serialization, ViewModel) ---
            implementation(libs.koin.core)                   // Koin Core DI

            implementation(libs.ktor.client.core)            // Ktor Networking Core
            implementation(libs.ktor.client.contentNegotiation) // Ktor Content Negotiation
            implementation(libs.ktor.serialization.kotlinxJson) // Ktor JSON Serialization
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.datetime)

            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)


            implementation(libs.voyager.navigator)       // Core navigation components
            implementation(libs.voyager.screen.model)    // Lifecycle management for ViewModels/ScreenModels
            implementation(libs.voyager.koin)
            implementation(libs.mp.settings.noarg) //share preference for kmp

        }
        val iosMain by creating {
            dependencies{
                implementation(libs.ktor.client.darwin) // Ktor HTTP Client Engine for iOS
                implementation(libs.sqldelight.driver.native) // iOS driver
            }
        }


        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }


}


android {
    namespace = "com.example.taofamily"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.taofamily"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}




dependencies {
    debugImplementation(compose.uiTooling)
}

