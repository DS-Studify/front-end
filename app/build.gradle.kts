import com.ds.studify.convention.implementation

plugins {
    alias(libs.plugins.studify.app)
    alias(libs.plugins.studify.hilt)
    alias(libs.plugins.studify.compose.app)
    alias(libs.plugins.studify.serialization)
}

android {
    namespace = "com.ds.studify"

    defaultConfig {
        applicationId = "com.ds.studify"
        versionCode = 1
        versionName = "1.0"

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.resources)

    implementation(projects.feature.home)
    implementation(projects.feature.camera)
    implementation(projects.feature.statistics)
    implementation(projects.feature.analysis)
    implementation(projects.feature.login)
    implementation(projects.feature.signup)
    implementation(projects.feature.mypage)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.bundles.orbit)
}