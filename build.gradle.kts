plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.1.0"
}

group = "lol.koblizek"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "neoforgedReleases"
        url = uri("https://maven.neoforged.net/releases")
    }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://nexus.velocitypowered.com/repository/maven-public/") }
}

dependencies {
    implementation("com.github.MCPHackers:DiffPatch:cde1224")
    implementation("net.neoforged:AutoRenamingTool:1.0.7")
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