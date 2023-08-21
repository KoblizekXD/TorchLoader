plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
    `java-gradle-plugin`
}

group = "lol.koblizek"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.fabricmc.net/")
    }
}

dependencies {
    api("cuchaz:enigma:2.3.1")
    implementation("commons-io:commons-io:2.13.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.apache.commons:commons-lang3:3.13.0")
    implementation(gradleApi())
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
gradlePlugin {
    plugins {
        create("torch-loader") {
            id = "torch-loader"
            implementationClass = "lol.koblizek.torch.plugin.TorchLoaderPlugin"
        }
    }
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "lol.koblizek"
            artifactId = "torch-loader"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}