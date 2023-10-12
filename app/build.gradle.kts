plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlinx.kover")
}

android {
    namespace = "fr.delcey.todok"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.delcey.todok"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true

        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    // Android

    // XML
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.fragment:fragment-ktx:1.5.2")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("com.google.android.material:material:1.6.1")
    kapt("androidx.room:room-compiler:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.7.1")


    // Unit tests

    testImplementation("androidx.arch.core:core-testing:2.1.0") {
        // Removes the Mockito dependency bundled with arch core (wtf android ??)
        exclude("org.mockito", "mockito-core")
    }
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.0-Beta")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.12.7")
    testImplementation("app.cash.turbine:turbine:1.0.0")


    // Android tests

    androidTestImplementation("androidx.arch.core:core-testing:2.1.0") {
        // Removes the Mockito dependency bundled with arch core (wtf android ??)
        exclude("org.mockito", "mockito-core")
    }
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
}

koverAndroid {
    common {
        filters {
            excludes {
                annotatedBy(
                    "dagger.Module",
                    "dagger.internal.DaggerGenerated",
                    "androidx.room.Database",
                )
                packages(
                    "hilt_aggregated_deps", // Hilt: GeneratedInjectors (NOT annotated by DaggerGenerated)
                    "fr.delcey.todok.databinding", // ViewBinding
                )
                classes(
                    // Hilt
                    // TODO Nino Delete when this is fixed: https://github.com/Kotlin/kotlinx-kover/issues/331
                    "*_*Factory\$*",

                    // Room
                    "*_Impl",
                    "*_Impl\$*",

                    // TODO Nino Delete when this is fixed: https://github.com/Kotlin/kotlinx-kover/issues/331
                    "*AppDatabase\$*",

                    // Gradle Generated
                    "fr.delcey.todok.BuildConfig", // TODO Nino: Why Kover doesn't understand tests on BuildConfig ?!
                    "fr.delcey.todok.data.BuildConfigResolver",

                    // Utils
                    "fr.delcey.todok.ui.utils*", // TODO Nino: UnitTest utils package

                    // TODO Nino: Remove below when Kover can handle Android integration tests!
                    // Android UI
                    "*MainApplication",
                    "*MainApplication\$*",
                    "*Fragment",
                    "*Fragment\$*",
                    "*Activity",
                    "*Activity\$*",
                    "*Adapter",
                    "*Adapter\$*",
                )
            }
        }
    }
}