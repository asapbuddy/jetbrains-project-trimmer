plugins {
    id("java")
    id("org.jetbrains.intellij.platform")
}

group = "dev.asapbuddy"
version = "0.1.2"

val localRiderPath = providers.gradleProperty("localRiderPath")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    intellijPlatform {
        if (localRiderPath.isPresent) {
            local(localRiderPath)
        } else {
            rider("2024.3") {
                useInstaller = false
            }
        }
    }

    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

intellijPlatform {
    buildSearchableOptions = false
    instrumentCode = false

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "243"
            untilBuild = provider { null }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<JavaCompile> {
        options.release.set(21)
    }
}
