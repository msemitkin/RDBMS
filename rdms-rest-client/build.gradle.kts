plugins {
    id("org.openapi.generator") version ("6.2.0")
    id("application")
    id("org.openjfx.javafxplugin") version ("0.0.13")
}

group = "com.github.msemitkin"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-graphics:19")
    implementation("org.springframework.boot:spring-boot-starter:2.7.4")
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.7.4")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.4")
    implementation("io.swagger:swagger-annotations:1.5.22")
    implementation("org.openapitools:jackson-databind-nullable:0.2.3")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val generatedSourcesDir = "$buildDir/generated/openapi"

openApiGenerate {
    validateSpec.set(true)
    inputSpec.set("${project.projectDir}/../rdms-openapi/src/main/resources/openapi.yaml")
    generatorName.set("java")
    outputDir.set(generatedSourcesDir)
    packageName.set("ua.knu.csc.it.rdms")
    apiPackage.set("ua.knu.csc.it.rdms.api")
    modelPackage.set("ua.knu.csc.it.rdms.model")
    modelNameSuffix.set("Dto")
    configOptions.set(
        mutableMapOf(
            "library" to "webclient",
            "performBeanValidation" to "true"
        )
    )
}


tasks.getByName("openApiGenerate") {
    doFirst {
        delete(generatedSourcesDir)
    }
    doLast {
        delete(
            "$generatedSourcesDir/api",
            "$generatedSourcesDir/gradle",
            "$generatedSourcesDir/gradle",
            "$generatedSourcesDir/.github",
            "$generatedSourcesDir/.gitignore",
            "$generatedSourcesDir/.travis.yml",
            "$generatedSourcesDir/build.sbt",
            "$generatedSourcesDir/git_push.sh",
            "$generatedSourcesDir/gradle.properties",
            "$generatedSourcesDir/gradlew",
            "$generatedSourcesDir/gradlew.bat",
            "$generatedSourcesDir/pom.xml",
            "$generatedSourcesDir/settings.gradle",
            "$generatedSourcesDir/.openapi-generator",
            "$generatedSourcesDir/.openapi-generator-ignore",
            "$generatedSourcesDir/README.md",
            "$generatedSourcesDir/src/main/AndroidManifest.xml",
            "$generatedSourcesDir/src/test"
        )
    }
}

sourceSets {
    getByName("main") {
        java {
            srcDir("$generatedSourcesDir/src/main/java")
        }
    }
}

javafx {
    version = "19"
    modules = listOf(
        "javafx.controls"
    )
}

application {
    mainClass.set("com.github.msemitkin.rdms.RdmsDesktopApplication")
}
