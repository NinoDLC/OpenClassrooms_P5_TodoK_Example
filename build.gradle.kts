import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}")
        classpath("org.jetbrains.kotlinx:kover:${Versions.KOVER}")
    }
}

plugins {
    id("org.jetbrains.kotlinx.kover") version Versions.KOVER
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "kover")
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

koverMerged {
    enable()

    filters {
        classes {
            excludes.addAll(
                listOf(
                    // Hilt
                    "dagger.hilt.*",
                    "hilt_aggregated_deps.*",
                    "*_Factory",
                    "*_Factory\$*",
                    "*_*Factory",
                    "*_*Factory\$*",
                    "*_Impl",
                    "*_Impl\$*",
                    "*.DataModule",
                    "*.DataModule\$*",
                    "*_MembersInjector",
                    "*_HiltModules",
                    "*_HiltModules\$*",

                    // Gradle Generated
                    "fr.delcey.todok.BuildConfig", // TODO Nino: Why Kover doesn't understand tests on BuildConfig ?!
                    "fr.delcey.todok.data.BuildConfigResolver", // Can't mockK static field: https://github.com/mockk/mockk/issues/107

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
                    "*AppDatabase",
                    "*AppDatabase\$*",
                    "fr.delcey.todok.databinding.*", // ViewBinding
                    "*Adapter",
                    "*Adapter\$*",
                )
            )
        }
    }
}