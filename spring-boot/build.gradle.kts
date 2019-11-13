version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("plugin.jpa")
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")

    implementation("org.springframework:spring-jms")
    implementation("com.amazonaws:aws-java-sdk:1.11.665")
    implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.8")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.github.microutils:kotlin-logging:1.7.6")

    implementation("com.monitorjbl:xlsx-streamer:2.1.0")

    runtimeOnly("com.h2database:h2:1.4.200")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    implementation("cloud.localstack:localstack-utils:0.1.22")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}