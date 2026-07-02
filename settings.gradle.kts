pluginManagement {
    plugins {
        id("name.remal.jacoco-to-cobertura") version "2.0.4"
    }
}

plugins {
    id("com.gradleup.nmcp.settings") version "1.6.1"
}

rootProject.name = "sstemplates"

include("sstemplates-core")
include("sstemplates-examples")

nmcpSettings {
    centralPortal {
        username = providers.gradleProperty("centralPortalUsername")
        password = providers.gradleProperty("centralPortalPassword")
        publishingType = "AUTOMATIC"
    }
}
