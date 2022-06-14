plugins {
    java
}

group = "io.rokuko"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        name = "spigotmc-repo"
        setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven{
        setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    mavenCentral()
}

dependencies {

    // spigot-api
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    testCompileOnly("org.projectlombok:lombok:1.18.8")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.8")
}

tasks.jar.configure {
    destinationDirectory.set(File("C:\\Users\\ASUS\\Desktop\\Server\\1.12.2\\plugins"))
}