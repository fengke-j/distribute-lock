plugins {
    id("java")
}

group = "com.fk.distribute.lock"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("D:\\maven\\repository")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter-web:3.5.0")
    implementation("com.fk.distribute.lock:distribute-lock-starter:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}