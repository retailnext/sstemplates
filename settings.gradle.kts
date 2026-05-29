plugins {
    id("com.gradleup.nmcp.settings") version "1.5.0"
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
