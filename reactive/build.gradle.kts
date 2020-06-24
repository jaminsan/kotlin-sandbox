version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom("io.r2dbc:r2dbc-bom:Arabba-SR5")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.7")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")

    implementation("org.springframework.data:spring-data-r2dbc:1.1.1.RELEASE")
    implementation("io.r2dbc:r2dbc-h2:0.8.4.RELEASE")
    implementation("dev.miku:r2dbc-mysql:0.8.1.RELEASE")
    runtimeOnly("mysql:mysql-connector-java")

    implementation("org.flywaydb:flyway-core")

    implementation("io.github.microutils:kotlin-logging:1.7.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.r2dbc:r2dbc-h2:0.8.4.RELEASE")
}
