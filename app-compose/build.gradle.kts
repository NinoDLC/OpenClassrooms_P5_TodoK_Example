plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlinx.kover")
}

android {
    namespace = "fr.delcey.todok.compose"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.delcey.todok.compose"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
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
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    // Hilt needs the "full picture" to perform its compile-time checks so we have to import all the related projects
    implementation(project(":ui-compose"))
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-compiler:2.48.1")

    // region Hilt x Worker https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.1.0")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    // endregion
}

// region Kover
dependencies {
    kover(project(":data"))
    kover(project(":domain"))
    kover(project(":ui-compose"))
}

koverReport {
    filters {
        excludes {
            annotatedBy(
                "dagger.Module",
                "dagger.internal.DaggerGenerated",
                "androidx.room.Database",
            )
            packages(
                "hilt_aggregated_deps", // Hilt: GeneratedInjectors (NOT annotated by DaggerGenerated)
                "fr.delcey.todok.uicompose.databinding", // ViewBinding
            )
            classes(
                // COMMON
                // Hilt
                // TODO Nino Delete when this is fixed: https://github.com/Kotlin/kotlinx-kover/issues/331
                "*_*Factory\$*",

                // Gradle Generated
                "fr.delcey.todok.*.BuildConfig", // TODO Nino: Why Kover doesn't understand tests on BuildConfig ?!


                // DATA
                // Room
                "*_Impl",
                "*_Impl\$*",

                // TODO Nino Delete when this is fixed: https://github.com/Kotlin/kotlinx-kover/issues/331
                "*AppDatabase\$*",
                "fr.delcey.todok.data.config.BuildConfigResolver",


                // UI XML
                // Utils
                "fr.delcey.todok.uicompose.utils*", // TODO Nino: UnitTest utils package

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
// endregion Kover