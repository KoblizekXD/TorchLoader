plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
    `java-gradle-plugin`
}

group = "lol.koblizek"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(gradleApi())
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
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