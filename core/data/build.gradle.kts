import com.ds.studify.convention.implementation
import java.util.Properties

plugins {
    alias(libs.plugins.studify.module)
    alias(libs.plugins.studify.hilt)
    alias(libs.plugins.studify.paging)
    alias(libs.plugins.studify.serialization)
    alias(libs.plugins.studify.retrofit)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.ds.studify.core.data"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", properties["base.url"].toString())
        }

        release {
            buildConfigField("String", "BASE_URL", properties["base.url"].toString())
        }
    }
}

dependencies {
    implementation(projects.core.resources)
    implementation(projects.core.domain)
}