@file:Suppress("UnstableApiUsage")


val license: String by project
val minecraftVersion: String by project
val modVersion: String by project
val modPackage: String by project
val modId: String by project
val modName: String by project
val modAuthor: String by project
val modDescription: String by project
val forgeVersion: String by project
val kjsVersion: String by project
val eioVersion: String by project
val jeiVersion: String by project
val githubUser: String by project
val githubRepo: String by project

plugins {
    id("net.neoforged.moddev.legacyforge") version "2.0.140"
    id("com.github.gmazzo.buildconfig") version "4.0.4"
    java
    `maven-publish`
}

// cannot be configured inside the block for some reason
legacyForge.version = "$minecraftVersion-$forgeVersion"

base {
    version = "$minecraftVersion-$modVersion"
    archivesName.set("$modId-forge")
}

legacyForge {
    runs {
        configureEach {
            // DCEVM hot-swapping
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            jvmArgument("-XX:+IgnoreUnrecognizedVMOptions")
        }

        create("client") {
            client()
        }
        create("server") {
            server()
        }
    }
    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

mixin {
    add(sourceSets.main.get(), "$modId.mixins.refmap.json")
    config("$modId.mixins.json")
}

repositories {
    maven("https://maven.latvian.dev/releases") // KubeJS and Rhino for KubeJS
    maven("https://maven.architectury.dev") // Architectury for KubeJS
    maven("https://maven.rover656.dev/releases") // EnderIO
    maven("https://dogforce-games.com/maven") // GraphLib for EnderIO
    maven("https://maven.tterrag.com") // Registrate for EnderIO
    maven("https://maven.blamejared.com") // JEI
    mavenLocal()
}

dependencies {
    // Mixin
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    // KubeJS
    modImplementation("dev.latvian.mods:kubejs-forge:$kjsVersion")
    modImplementation("dev.latvian.mods:rhino-forge:2001.2.2-build.17")
    modRuntimeOnly("dev.architectury:architectury-forge:9.1.12")
    runtimeOnly("io.github.llamalad7:mixinextras-forge:0.2.0-rc.4")

    // EnderIO
    modImplementation("com.enderio:EnderIO:$minecraftVersion-$eioVersion")
    modRuntimeOnly("com.tterrag.registrate:Registrate:MC1.20-1.3.11")
    runtimeOnly("dev.gigaherz.graph:GraphLib3:3.0.4")

    // JEI
    modRuntimeOnly("mezz.jei:jei-$minecraftVersion-forge:$jeiVersion") { isTransitive = false }
}

tasks {
    processResources {
        val resourceTargets = listOf("META-INF/mods.toml", "pack.mcmeta")

        val replaceProperties = mapOf(
            "license" to license,
            "minecraftVersion" to minecraftVersion,
            "version" to project.version as String,
            "modId" to modId,
            "modName" to modName,
            "modAuthor" to modAuthor,
            "modDescription" to modDescription,
            "forgeVersion" to forgeVersion,
            // use major version for FML only because wrong Forge version error message
            // is way better than FML error message
            "forgeFMLVersion" to forgeVersion.substringBefore("."),
            "kjsVersion" to kjsVersion,
            "eioVersion" to eioVersion,
            "githubUser" to githubUser,
            "githubRepo" to githubRepo
        )

        println("[Process Resources] Replacing resource properties: ")
        replaceProperties.forEach { (key, value) -> println("\t -> $key = $value") }

        inputs.properties(replaceProperties)
        filesMatching(resourceTargets) {
            expand(replaceProperties)
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<GenerateModuleMetadata> {
        enabled = false
    }

    jar {
        manifest.attributes(mapOf("MixinConfigs" to "${modId}.mixins.json"))
    }
}

extensions.configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

buildConfig {
    buildConfigField("String", "MOD_ID", "\"$modId\"")
    buildConfigField("String", "MOD_NAME", "\"$modName\"")
    buildConfigField("String", "MOD_VERSION", "\"$version\"")
    packageName(modPackage)
    className("KubeIOConstants")
    useJavaOutput()
}
