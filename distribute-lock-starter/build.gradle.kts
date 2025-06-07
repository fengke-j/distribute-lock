plugins {
    id("java")
    `maven-publish`
}

group = "com.fk.distribute.lock"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.springframework.boot:spring-boot-starter-data-redis:3.5.0")
    compileOnly("org.redisson:redisson-spring-boot-starter:3.49.0")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.5.0")
    compileOnly("org.springframework.boot:spring-boot-starter-aop:3.5.0")
    runtimeOnly("org.aspectj:aspectjweaver:1.9.24")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "localRepository"
            url = uri(layout.buildDirectory.dir("D:\\maven\\repository")) // 本地仓库路径
        }
    }
    publications {
        create<MavenPublication>("anotherMaven") {
            from(components["java"])
            groupId = "com.fk.distribute.lock" // 组织ID
            artifactId = "distribute-lock-starter" // 项目ID
            version = "1.0.0" // 版本号
        }
    }
}