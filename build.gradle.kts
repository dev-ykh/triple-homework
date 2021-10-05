import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.30"
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.google.cloud.tools.jib") version "1.6.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("kapt") version kotlinVersion
}

group = "dev.ykh"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val snippetsDir by extra { file("build/generated-snippets") }
val kotlinVersion = "1.4.30"
val jasyncMysqlVersion = "1.1.7"
val springmockkVersion = "3.0.1"
val mockkVersion = "1.10.5"
val asciidoctorVersion = "2.0.5.RELEASE"

dependencyManagement {
    val springCloudVersion = "2020.0.3"
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
        implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")

        implementation("com.github.jasync-sql:jasync-r2dbc-mysql:$jasyncMysqlVersion") {
            exclude(group = "io.netty")
        }

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(module = "junit")
            exclude(module = "mockito-core")
        }
        testImplementation("com.ninja-squad:springmockk:$springmockkVersion")
        testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
        testImplementation("org.springframework.security:spring-security-test")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
        testImplementation("io.projectreactor:reactor-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
        testImplementation("io.mockk:mockk:$mockkVersion")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.test {
        useJUnitPlatform()
        outputs.dir(snippetsDir)
    }
}
