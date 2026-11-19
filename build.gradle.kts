plugins {
    id("net.neoforged.moddev") version "2.0.143"
    id("com.almostreliable.almostgradle") version "2.4.0"
}

almostgradle.setup {
    withSourcesJar = false
    downloadSources = true
    downloadJavadoc = true
}

repositories {
    // KubeJS
    maven("https://maven.latvian.dev/releases")
    maven("https://maven.latvian.dev/mirror")

    // EnderIO
    maven("https://maven.rover656.dev/releases")

    mavenLocal()
}

dependencies {
    // KubeJS
    implementation("dev.latvian.mods:kubejs-neoforge:${almostgradle.getProperty("kjsVersion")}")?.let {
        interfaceInjectionData(it)
    }

    // EnderIO
    implementation("com.enderio:enderio:${almostgradle.getProperty("eioVersion")}")
    implementation("com.enderio:enderio-modded-conduits:${almostgradle.getProperty("eioVersion")}") {
        isTransitive = false
    }

    // Applied Energistics 2
    localImplementation("org.appliedenergistics:appliedenergistics2:${almostgradle.getProperty("aeVersion")}")
}
