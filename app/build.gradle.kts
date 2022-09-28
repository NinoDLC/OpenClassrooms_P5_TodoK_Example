plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
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

kover {
    instrumentation {
        excludeTasks.add("testReleaseUnitTest")
    }
}