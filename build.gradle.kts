plugins {
    id("net.neoforged.moddev") version "2.0.30-beta"
    id("com.almostreliable.almostgradle") version "1.1.1"
}

almostgradle.setup {
    withSourcesJar = false
}

repositories {
    // KubeJS
    maven("https://maven.latvian.dev/releases")
    maven("https://jitpack.io") { // Animated Gif Library
        content {
            includeGroup("com.github.rtyley")
        }
    }

    // EnderIO
    maven("https://maven.rover656.dev/releases")

    mavenLocal()
}

dependencies {
    // KubeJS
    implementation("dev.latvian.mods:kubejs-neoforge:${almostgradle.getProperty("kjsVersion")}")

    // EnderIO
    implementation("com.enderio:enderio-machines:${almostgradle.getProperty("eioVersion")}")
    implementation("com.enderio:enderio-conduits:${almostgradle.getProperty("eioVersion")}")
    implementation("com.enderio:enderio-conduits-modded:${almostgradle.getProperty("eioVersion")}") {
        isTransitive = false
    }
}
