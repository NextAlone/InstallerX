pluginManagement {
    repositories {
        mavenLocal()
//        maven { setUrl("https://maven.aliyun.com/repository/public/") }
//        maven { setUrl("https://jitpack.io") }
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { setUrl("https://maven.scijava.org/content/repositories/public/") }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
//        maven { setUrl("https://maven.aliyun.com/repository/public/") }
//        maven { setUrl("https://jitpack.io") }
        google()
        mavenCentral()
        maven { setUrl("https://maven.scijava.org/content/repositories/public/") }
    }
}

rootProject.name = "InstallerX"
include(
    ":app",
    ":hidden-api"
)
