plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.iries.youtubealarm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.iries.youtubealarm"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(arrayOf("x86", "x86_64",
                    "armeabi-v7a", "arm64-v8a"))
        }

        splits {
            abi {
                isEnable = true
                reset()
                include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
                isUniversalApk = true
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
        }

        jniLibs {
            useLegacyPackaging = true
        }
    }

}


buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}

dependencies {
    //firebase dependencies for sha-1
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.3.1")

    implementation("com.google.http-client:google-http-client-gson:1.26.0")
    //dependency for Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    //dependencies for youtube api
    implementation("com.google.api-client:google-api-client-android:2.2.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20240123-2.0.0") {
        exclude("org.apache.httpcomponents")
    }

    implementation("androidx.lifecycle:lifecycle-service:2.7.0")

    implementation("com.cedarsoftware:json-io:4.10.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.preference:preference:1.2.1")

    //mp3 converter dependencies
    implementation("com.github.yausername.youtubedl-android:library:0.15.0")
    implementation("com.github.yausername.youtubedl-android:ffmpeg:0.15.0")

    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
