plugins {
    id("java")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":rdms-core"))
    implementation(project(":rdms-file-system"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
    implementation("org.springframework.boot:spring-boot-starter:2.7.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}