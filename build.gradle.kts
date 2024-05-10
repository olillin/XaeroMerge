import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.24"
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.olillin.xaeromerge"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":jm-to-xaero"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.getByName("build").dependsOn(tasks.getByName("shadowJar"))

tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "com.olillin.xaeromerge.AppKt")
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier = null

    mustRunAfter(":jm-to-xaero:shadowJar")
}

application {
    mainClass = "com.olillin.xaeromerge.AppKt"
}

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls")
}

tasks.named("startScripts") {
    dependsOn(":shadowJar")
    mustRunAfter(":jm-to-xaero:shadowJar")
}
tasks.named("startShadowScripts") {
    dependsOn(":jar")
}
