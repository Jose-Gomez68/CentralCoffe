plugins {
    id("com.android.application")
    kotlin("kapt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.salestapapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.salestapapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    //var
    val lifecycle_version = "2.7.0"
    val room_version = "2.6.1"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    //viewmodel and liveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    //room
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    //gson convert
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}