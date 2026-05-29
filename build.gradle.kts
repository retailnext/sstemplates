plugins {
    id("com.gradleup.nmcp.aggregation")
}

group = "net.retailnext"
version = providers.gradleProperty("version").getOrElse("2.0.0-SNAPSHOT")

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of(17)
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            // Buffer test output — only emit on failure (keeps CI logs clean)
            testLogging {
                showStandardStreams = false
            }
            reports {
                junitXml.required = true
            }
        }

        tasks.withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        tasks.withType<Javadoc> {
            (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
        }
    }
}
