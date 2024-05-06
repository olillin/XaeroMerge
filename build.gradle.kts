plugins {
    kotlin("jvm") version "1.9.21"
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.olillin.xaeromerge"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainModule = "com.olillin.xaeromerge.app"
    mainClass = "com.olillin.xaeromerge.AppKt"
}

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls")
}