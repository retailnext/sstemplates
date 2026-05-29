plugins {
    `java-library`
    `maven-publish`
    signing
}

dependencies {
    api(libs.commons.beanutils)
    api(libs.commons.digester3)
    api(libs.commons.lang3)
    api(libs.juel.api)
    api(libs.juel.impl)
    api(libs.juel.spi)
    api(libs.log4j.api)
    api(libs.poi)

    compileOnly(libs.jakarta.servlet.api)
    compileOnly(libs.jakarta.servlet.jsp.api)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.jakarta.servlet.api)
    testImplementation(libs.log4j.core)
    testImplementation(libs.spring.core)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.web)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name = "SSTemplates Core"
                description = "SSTemplates produces Excel documents using XML templates."
                url = "https://github.com/retailnext/sstemplates"

                licenses {
                    license {
                        name = "Apache License 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                        distribution = "repo"
                    }
                }

                developers {
                    developer {
                        name = "RetailNext, Inc."
                        email = "sstemplates-maintainers@retailnext.net"
                        organization = "RetailNext, Inc."
                        organizationUrl = "https://retailnext.net/"
                    }
                }

                scm {
                    connection = "scm:git:https://github.com/retailnext/sstemplates.git"
                    url = "https://github.com/retailnext/sstemplates"
                }
            }
        }
    }

    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/retailnext/sstemplates")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: ""
            }
        }
    }
}

signing {
    val signingKey = providers.gradleProperty("signingInMemoryKey").orNull
    val signingPassword = providers.gradleProperty("signingInMemoryKeyPassword").orNull
    if (!signingKey.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["mavenJava"])
    }
}
