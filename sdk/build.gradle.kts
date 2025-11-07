plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "co.qredet.sdk"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    implementation("com.squareup:otto:1.3.8")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.6")
    implementation("com.github.elvis-chuks.credit-Card-Nfc-Reader:library:parent-2.1.1")

    // Backend API integration dependencies - commented out for now
    // implementation("com.squareup.okhttp3:okhttp:4.9.1")
    // implementation("com.google.code.gson:gson:2.8.8")
}

configure<PublishingExtension> {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
    }

    publications {
        create<MavenPublication>("Maven") {
            artifactId = "qredet-sdk-android"
            groupId = "com.github.Qredet"
            version = "1.0.6"
            afterEvaluate {
                artifact(tasks.getByName(
                    "bundleReleaseAar"
                ))
            }
        }

        withType<MavenPublication> {
            pom {
                packaging = "jar"
                name.set("sdk")
                description.set("Qredet contactless sdk")
                url.set("qredet.com")
                licenses {
                    license {
                        name.set("MIT license")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        name.set("Taiwo Farawe")
                        email.set("farawehassan@yahoo.com")
                    }
                }
            }
        }
    }
}
