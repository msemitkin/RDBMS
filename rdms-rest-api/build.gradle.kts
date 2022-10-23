plugins {
    id("java")
    id("org.openapi.generator") version "6.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":rdms-core"))
    implementation(project(":rdms-file-system"))
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.4")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.4")
    implementation("org.openapitools:jackson-databind-nullable:0.2.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val generatedSourcesDir = "$buildDir/generated/openapi"

openApiGenerate {
    validateSpec.set(true)
    inputSpec.set("${project.projectDir}/../rdms-openapi/src/main/resources/openapi.yaml")
    generatorName.set("spring")
    outputDir.set(generatedSourcesDir)
    packageName.set("ua.knu.csc.it.rdms")
    apiPackage.set("ua.knu.csc.it.rdms.api")
    modelPackage.set("ua.knu.csc.it.rdms.model")
    modelNameSuffix.set("Dto")
    configOptions.set(
        mutableMapOf(
            "basePackage" to "ua.knu.csc.it.rdms",
            "configPackage" to "ua.knu.csc.it.rdms.configuration",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "performBeanValidation" to "true"
//            "hateoas" to "false"
        )
    )
}

tasks.getByName("openApiGenerate") {
    doFirst {
        delete(generatedSourcesDir)
    }
    doLast {
        delete(
            "$generatedSourcesDir/.openapi-generator",
            "$generatedSourcesDir/.openapi-generator-ignore",
            "$generatedSourcesDir/README.md"
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