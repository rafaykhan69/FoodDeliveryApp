plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.fooddelivery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fooddelivery"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-ads:23.2.0")

    implementation("com.google.firebase:firebase-analytics:21.2.0")
    implementation("com.google.firebase:firebase-auth:21.3.0")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation("com.google.firebase:firebase-storage:20.2.0")
    implementation("com.google.firebase:firebase-messaging:23.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("com.firebaseui:firebase-ui-database:8.0.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.hbb20:ccp:2.7.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Ensure consistent Kotlin version
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
}


configurations.all {
    resolutionStrategy.eachDependency {
        val requested = requested
        if (requested.group == "androidx.appcompat" && requested.name == "appcompat") {
            useVersion("1.6.1")
        }
        if (requested.group == "androidx.activity" && requested.name == "activity") {
            useVersion("1.7.2")
        }
        // Add more exclusion rules if needed
    }
}
