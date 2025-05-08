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
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:resources"))

    implementation(projects.feature.home)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}