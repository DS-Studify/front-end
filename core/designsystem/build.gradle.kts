import com.ds.studify.convention.implementation

plugins {
    alias(libs.plugins.studify.module)
    alias(libs.plugins.studify.compose.module)
    alias(libs.plugins.studify.serialization)
}

android {
    namespace = "com.ds.studify.core.designsystem"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.resources)
    implementation(projects.core.uiExtension)
    implementation(projects.core.domain)
}