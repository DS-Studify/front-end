plugins {
    alias(libs.plugins.studify.module)
    alias(libs.plugins.studify.hilt)
    alias(libs.plugins.studify.compose.module)
    alias(libs.plugins.studify.serialization)
}

android {
    namespace = "com.ds.studify.feature.camera"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.resources)

    implementation(libs.bundles.camera)
}