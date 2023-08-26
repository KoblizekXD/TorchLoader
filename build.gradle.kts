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
    implementation("org.vineflower:vineflower:1.9.2")
    implementation("net.fabricmc:tiny-remapper:0.8.7")
    implementation("net.fabricmc:mapping-io:0.4.2")
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
    website = "https://github.com/KoblizekXD/TorchLoader"
    vcsUrl = "https://github.com/KoblizekXD/TorchLoader"

    plugins {
        create("torch-loader") {
            id = "lol.koblizek.torch-loader"
            displayName = "Torch Loader"
            description = "Plugin which makes creating Minecraft development much easier!"
            implementationClass = "lol.koblizek.torch.plugin.TorchLoaderPlugin"
            tags = listOf("minecraft", "modding", "decompilation", "deobfuscation")
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