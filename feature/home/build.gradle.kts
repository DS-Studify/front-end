plugins {
    alias(libs.plugins.studify.module)
    alias(libs.plugins.studify.hilt)
    alias(libs.plugins.studify.compose.module)
    alias(libs.plugins.studify.serialization)
}

android {
    namespace = "com.ds.studify.feature.home"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:resources"))
}