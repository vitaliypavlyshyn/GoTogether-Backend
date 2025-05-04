
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.cio.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.cio)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    // серіалізація
    implementation("io.ktor:ktor-serialization-gson:3.1.1")

    // datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:0.38.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.38.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.1")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.38.1")

    // postgresql драйвер
    implementation("org.postgresql:postgresql:42.5.1")

    // логування
    implementation("org.slf4j:slf4j-simple:2.0.9")

    // пул з'єднань
    implementation("com.zaxxer:HikariCP:5.0.1")

    // криптування паролів
    implementation("at.favre.lib:bcrypt:0.9.0")

}
