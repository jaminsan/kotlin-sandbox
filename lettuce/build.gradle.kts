version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("io.lettuce:lettuce-core:5.2.0.RELEASE")
    implementation("io.github.microutils:kotlin-logging:1.7.6")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
    implementation("org.slf4j:slf4j-log4j12:2.0.0-alpha1")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.testcontainers:testcontainers:1.12.2")
    testImplementation("org.testcontainers:junit-jupiter:1.12.2")
}
