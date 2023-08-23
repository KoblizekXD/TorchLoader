# TorchLoader  
Gradle plugin for maintaining Torch Loader API. Many parts of code were taken from its legacy predecessor, 
which was abandoned, due to problems found in code and rewrite was recommended.  

### Installation  
TODO :(  

### Usage  
Though TorchLoader is mainly used for mod development, it also supports decompiling Minecraft's source code to your 
projects directory, meaning full access to its source code(Don't forget, that redistribution of that code is **violation**
of EULA). You can find here a temporary code sample, how would implementation of the plugin would look like.  
```kotlin
minecraft {
    version = "1.19.2"
    mappings = "yarn"
    // Only if developing client or api
    development {
        // Modify fields
    }
}
```

### Contributing  
Standard contributing rules apply, to test the plugin, 
execute the `publishing/publishToMavenLocal` task, which will release the plugin into your 
local repository.  

### Licensing  
Project is licensed under standard [MIT license](LICENSE.txt)