plugins {
    alias(libs.plugins.studify.module)
    alias(libs.plugins.studify.compose.module)
}

android {
    namespace = "com.ds.studify.core.ui.extension"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
}