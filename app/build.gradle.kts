plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlinx.kover")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "fr.delcey.todok"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.ExperimentalStdlibApi",
            )
        }
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
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))
    androidTestApi(platform(project(":depconstraints")))


    // Android

    implementation(Libs.APPCOMPAT)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.CORE_KTX)
    implementation(Libs.FRAGMENT_KTX)
    implementation(Libs.GSON)
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)
    implementation(Libs.HILT_WORK)
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    implementation(Libs.MATERIAL)
    kapt(Libs.ROOM_COMPILER)
    implementation(Libs.ROOM_KTX)
    implementation(Libs.WORK_HILT_COMPILER)
    implementation(Libs.WORK_RUNTIME_KTX)


    // Unit tests

    testImplementation(Libs.ARCH_CORE_TESTING) {
        // Removes the Mockito dependency bundled with arch core (wtf android ??)
        exclude("org.mockito", "mockito-core")
    }
    testImplementation(Libs.ASSERTK)
    testImplementation(Libs.COROUTINES_TEST)
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKK)
    testImplementation(Libs.TURBINE)


    // Android tests

    androidTestImplementation(Libs.ARCH_CORE_TESTING) {
        // Removes the Mockito dependency bundled with arch core (wtf android ??)
        exclude("org.mockito", "mockito-core")
    }
    androidTestImplementation(Libs.ANDROIDX_TEST_CORE)
    androidTestImplementation(Libs.ESPRESSO_CONTRIB)
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.EXT_JUNIT)
    androidTestImplementation(Libs.RULES)
    androidTestImplementation(Libs.RUNNER)
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