pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
        kotlin("jvm").version(extra["kotlinVersion"] as String)
        kotlin("plugin.serialization").version(extra["serializationVersion"] as String)
        id("org.jetbrains.compose").version(extra["composeVersion"] as String)
        id("io.ktor.plugin").version(extra["ktorVersion"] as String)
        id("com.google.dagger.hilt.android")
        id("dagger.hilt.android.plugin")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "dagger.hilt.android.plugin") {
                useModule("com.google.dagger:hilt-android-gradle-plugin:2.39.1")
            }
            if (requested.id.id.startsWith("com.google.cloud.tools.appengine")) {
                useModule("com.google.cloud.tools:appengine-gradle-plugin:${requested.version}")
            }
        }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "uwconnect"
include("android", "server", "models")
