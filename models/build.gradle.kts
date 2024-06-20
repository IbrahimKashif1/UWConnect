val releaseVersion: String by project
val exposedVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "org.uwconnect"
version = releaseVersion

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}