import com.ds.studify.convention.implementation

plugins {
    alias(libs.plugins.studify.module)
    alias(libs.plugins.studify.hilt)
    alias(libs.plugins.studify.paging)
    alias(libs.plugins.studify.serialization)
}

android {
    namespace = "com.ds.studify.core.data"
}

dependencies {
    implementation(projects.core.resources)
}