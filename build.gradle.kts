plugins {
    java
}

group = "com.github.fullavatar"
version = "0.1.0"

val appData = System.getenv("APPDATA")
    ?: throw GradleException("APPDATA is not defined; the Hytale pre-release installation cannot be located.")
val hytalePackageDir = file("$appData/Hytale/install/pre-release/package")
val hytaleGameDir = hytalePackageDir.resolve("game/latest")
val hytaleServerJar = hytaleGameDir.resolve("Server/HytaleServer.jar")

if (!hytaleServerJar.isFile) {
    throw GradleException("HytaleServer.jar was not found at $hytaleServerJar")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    compileOnly(files(hytaleServerJar))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    archiveBaseName.set("Tetrahedron")
}
