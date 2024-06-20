import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

val exposedVersion: String by project
val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val slf4jVersion: String by project
val postgresqlDriverVersion: String by project
val releaseVersion: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.cloud.tools.appengine") version "2.4.3"
}

group = "com.uwconnect"
version = releaseVersion

application {
    mainClass.set("com.uwconnect.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(project(":models"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    implementation(platform("org.kotlincrypto.hash:bom:0.4.0"))
    implementation("org.kotlincrypto.hash:sha2")
    implementation("org.kotlincrypto.core:digest:0.4.0")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    implementation("org.postgresql:postgresql:$postgresqlDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")

    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    implementation("com.google.firebase:firebase-admin:9.2.0")
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/server-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}