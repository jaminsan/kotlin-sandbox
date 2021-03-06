import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    val kotlinVersion by extra { "1.3.70" }
    val junitJupiterVersion by extra { "5.6.2" }
    val log4jVersion by extra { "2.9.0" }
    val arrowVersion by extra { "0.10.5" }

    repositories {
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        maven("https://repo.spring.io/snapshot")
        maven("https://repo.spring.io/milestone")
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath("io.spring.gradle:dependency-management-plugin:1.0.9.RELEASE")
    }

}


plugins {
    base
    java
    kotlin("plugin.jpa") version "1.3.70" apply false
    kotlin("jvm") version "1.3.70" apply false
    id("org.springframework.boot") version "2.3.0.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
    kotlin("plugin.spring") version "1.3.50" apply false
}

allprojects {

    group = "con.example"

    val junitJupiterVersion: String by rootProject.extra
    val log4jVersion: String by rootProject.extra

    apply {
        plugin("kotlin")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    repositories {
        jcenter()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
        maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
        maven("https://repo.spring.io/snapshot")
        maven("https://repo.spring.io/milestone")
        maven("https://repo.spring.io/libs-milestone")
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))

        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")

        testRuntime("org.apache.logging.log4j:log4j-core:$log4jVersion")
        testRuntime("org.apache.logging.log4j:log4j-jul:$log4jVersion")
    }
}
