plugins {
    war
}

dependencies {
    implementation(project(":sstemplates-core"))
    implementation(libs.poi)

    compileOnly(libs.jakarta.servlet.api)
    compileOnly(libs.jakarta.servlet.jsp.api)
}
