# TorchLoader  
Gradle plugin for maintaining Torch Loader API. Many parts of code were taken from its legacy predecessor, 
which was abandoned, due to problems found in code and rewrite was recommended.  

### Installation  
It's currently [Usage](#usage-) how to install the plugin

### Usage  
Though TorchLoader is mainly used for mod development, it also supports decompiling Minecraft's source code to your 
projects directory, meaning full access to its source code(Don't forget, that redistribution of that code is **violation**
of EULA).   
First we need to add it into dependencies, to do so, open `settings.gradle.kts` file and add following lines before 
anything else:  
```kotlin
pluginManagement {
    repositories {
        // This will add gradlePluginPortal as repository, this is where torchloader plugin is stored at
        gradlePluginPortal()
    }
}
```  
Next, we add following lines to import TorchLoader plugin into gradle:
```kotlin
// build.gradle.kts
plugins {
    id("torch-loader") version("version to use") // Version will be in our case, the latest one from releases page
}
```  
Now we need to reload the project. Once that is done we can specify all the things we want:  
```kotlin
// build.gradle.kts
minecraft {
    version = "1.19.2"
    mappings = "yarn"
    // Only if developing client or api
    development {
        decompile = true // Decompiles game into source directory
    }
}
```

### Contributing  
Standard contributing rules apply, to test the plugin, 
execute the `publishing/publishToMavenLocal` task, which will release the plugin into your 
local repository.  

### Licensing  
Project is licensed under standard [MIT license](LICENSE.txt)