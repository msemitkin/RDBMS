plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":rdms-core"))
    implementation(project(":rdms-file-system"))
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.4")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}