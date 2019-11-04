version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("org.springframework:spring-jms")
    implementation("com.amazonaws:aws-java-sdk:1.11.665")
    implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.8")

    implementation("io.github.microutils:kotlin-logging:1.7.6")

    implementation("cloud.localstack:localstack-utils:0.1.22")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
