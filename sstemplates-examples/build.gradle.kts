plugins {
    war
}

dependencies {
    implementation(project(":sstemplates-core"))
    implementation(libs.poi)

    providedCompile(libs.jakarta.servlet.api)
    providedCompile(libs.jakarta.servlet.jsp.api)
}
