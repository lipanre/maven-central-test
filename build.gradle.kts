plugins {
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.15.0"
}

repositories {
    mavenLocal()
    maven { setUrl("https://maven.aliyun.com/repository/public/") }
    maven { setUrl("https://plugins.gradle.org/m2/") }
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}


tasks.javadoc {
    options.encoding="utf-8"
}

group = "io.github.lipanre"
version = "1.0"

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            artifactId = "maven-test"
            version = "1.0"
            from(components["java"])

            pom {
                name = "maven-test"
                description = "test deploy jar to maven central"
                url = "https://github.com/lipanre/maven-central-test"
                inceptionYear = "2024"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://github.com/lipanre/maven-central-test"
                    }

                    developers {
                        developer {
                            id = "lipanre"
                            name = "lipanre"
                        }
                    }
                    scm {
                        connection = "scm:git:https://github.com/lipanre/maven-central-test.git"
                        developerConnection = "scm:git:https://github.com/lipanre/maven-central-test.git"
                        url = "https://github.com/lipanre/maven-central-test"
                    }
                }
            }
        }
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

jreleaser {
    signing {
        setActive("ALWAYS")
        armored.set(true)
        setMode("FILE")
        publicKey = "/Users/lipan/devfiles/gpg/public.pgp"
        secretKey = "/Users/lipan/devfiles/gpg/private.pgp"
    }
    deploy {
        maven {

            mavenCentral {
                register("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}
