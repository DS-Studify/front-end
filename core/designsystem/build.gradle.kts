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
    implementation(project(":core:resources"))
}