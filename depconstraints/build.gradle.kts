/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("java-platform")
    id("maven-publish")
}

// Android

val appcompat = "1.4.0"
val constraintLayout = "2.1.4"
val coreKtx = "1.8.0"
val fragmentKtx = "1.5.2"
val gson = "2.9.1"
val hiltAndroid = Versions.HILT
val hiltCompiler = Versions.HILT
val hiltWork = "1.0.0"
val lifecycleLiveDataKtx = "2.5.1"
val material = "1.6.1"
val roomCompiler = Versions.ROOM
val roomKtx = Versions.ROOM
val workHiltCompiler = "1.0.0"
val workRuntimeKtx = "2.7.1"


// Tests

val archCoreTesting = "2.1.0" // TODO NINO How to remove mockito dep ?


// Unit tests

val assertk = "0.25"
val coroutinesTest = "1.7.0-Beta"
val junit = "4.13.2"
val mockk = "1.12.7"
val turbine = "1.0.0"


// Android tests

val androidxTestCore = "1.4.0"
val espressoContrib = "3.4.0"
val espressoCore = "3.4.0"
val extJunit = "1.1.3"
val rules = "1.4.0"
val runner = "1.4.0"


dependencies {
    constraints {
        // Android

        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraintLayout")
        api("${Libs.CORE_KTX}:$coreKtx")
        api("${Libs.FRAGMENT_KTX}:$fragmentKtx")
        api("${Libs.GSON}:$gson")
        api("${Libs.HILT_ANDROID}:$hiltAndroid")
        api("${Libs.HILT_COMPILER}:$hiltCompiler")
        api("${Libs.HILT_WORK}:$hiltWork")
        api("${Libs.LIFECYCLE_LIVE_DATA_KTX}:$lifecycleLiveDataKtx")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.ROOM_COMPILER}:$roomCompiler")
        api("${Libs.ROOM_KTX}:$roomKtx")
        api("${Libs.WORK_HILT_COMPILER}:$workHiltCompiler")
        api("${Libs.WORK_RUNTIME_KTX}:$workRuntimeKtx")


        // Tests

        api("${Libs.ARCH_CORE_TESTING}:$archCoreTesting")


        // Unit tests

        api("${Libs.ASSERTK}:$assertk")
        api("${Libs.COROUTINES_TEST}:$coroutinesTest")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.MOCKK}:$mockk")
        api("${Libs.TURBINE}:$turbine")


        // Android tests

        api("${Libs.ANDROIDX_TEST_CORE}:$androidxTestCore")
        api("${Libs.ESPRESSO_CONTRIB}:$espressoContrib")
        api("${Libs.ESPRESSO_CORE}:$espressoCore")
        api("${Libs.EXT_JUNIT}:$extJunit")
        api("${Libs.RULES}:$rules")
        api("${Libs.RUNNER}:$runner")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
