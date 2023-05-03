import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("application")
}

group = "com.vasanth.fscolorreplace"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application{
    mainClass.set("com.vasanth.fscolorreplace.FSColorReplace")
}

dependencies {
    implementation("com.opencsv:opencsv:5.7.1")
}