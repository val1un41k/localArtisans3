plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.localartisan3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.localartisan3"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //
    /////Photo and its elemets
    //
    implementation("io.coil-kt:coil-compose:2.6.0")

    //
    ////// Google Services

    //Firebase storage services

    implementation("com.google.firebase:firebase-storage-ktx:20.0.0")

    // Firebase auth serives
    implementation("com.google.firebase:firebase-auth:22.3.1")

    // Firebase firestore services
    implementation("com.google.firebase:firebase-firestore:23.0.3")

    // Firebase Bom
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore:24.11.1")
    //
    //Google Maps
    implementation ("com.google.android.gms:play-services-maps:18.1.0")

    //Google Compose
    implementation ("com.google.maps.android:maps-compose:2.7.2")

    implementation ("com.google.maps.android:android-maps-utils:2.3.0")

    implementation ("com.google.android.gms:play-services-location:21.2.0")
    //
    ////// material icons
    //
    implementation("androidx.compose.material:material-icons-extended:1.6.4")
    implementation("androidx.compose.material:material:1.6.4")
    //
    ////// courotines
    //
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    //
    ////// Android Navigation
    //
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // navigation compose
    implementation("androidx.navigation:navigation-compose:$nav_version")



}