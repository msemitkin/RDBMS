plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4") //TODO remove from core
    implementation("org.springframework:spring-context:5.3.23")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}