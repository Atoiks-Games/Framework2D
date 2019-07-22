# Framework2D

A game framework originally based on Java2D

Requires JDK 8 or above. Build with `./gradlew build`.

## When including Framework2D as a dependency

Can be fetched using https://jitpack.io.

Then you would pick one of the following (as this is a multi-module project) depending on the backend you want:

*   java2d: This one uses Java AWT, no native dependencies required
*   lwjgl3: This one uses LWJGL3 components. If you pick this one, remember to declare the runtime dependencies of version 3.2.2 on `lwjgl`, `lwjgl-glfw`, `lwjgl-opengl` and `lwjgl-stb`. Also might need to add the `-XstartOnFirstThread` flag when running the application!

Gradle will resolve the dependence on the core module, no need to explicitly declare it.

An example of a single-moduled project referencing the lwjgl module 1.0 release:

```groovy
// build.gradle

apply plugin: 'java'
apply plugin: 'application'

mainClassName = 'demo.App'

import org.gradle.internal.os.OperatingSystem

project.ext.lwjglVersion = "3.2.2"

switch (OperatingSystem.current()) {
    case OperatingSystem.LINUX:
        project.ext.lwjglNatives = "natives-linux"
        break
    case OperatingSystem.MAC_OS:
        project.ext.lwjglNatives = "natives-macos"
        break
    case OperatingSystem.WINDOWS:
        project.ext.lwjglNatives = "natives-windows"
        break
}

repositories {
    jcenter()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Atoiks-Games.Framework2D:lwjgl3:1.0'

    runtimeOnly "org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives"
    runtimeOnly "org.lwjgl:lwjgl-stb:$lwjglVersion:$lwjglNatives"
}

applicationDefaultJvmArgs = ["-XstartOnFirstThread"]
```