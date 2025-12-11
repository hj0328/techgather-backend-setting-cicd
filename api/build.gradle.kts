plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
}

dependencies {
	implementation(project(":domain"))
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation(platform("software.amazon.awssdk:bom:2.28.11"))
	implementation("software.amazon.awssdk:secretsmanager")
	implementation("software.amazon.awssdk:auth")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	runtimeOnly("com.mysql:mysql-connector-j")

    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
}

tasks.bootJar {
	enabled = true
}

tasks.jar {
	enabled = false
}

