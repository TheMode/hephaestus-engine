repositories {
    mavenLocal()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://jitpack.io/")
    mavenCentral()
}

dependencies {
    val minestom = "com.github.Minestom:Minestom:549a9a9b52"

    api(project(":core"))
    compileOnly(minestom)

    testImplementation(minestom)
}