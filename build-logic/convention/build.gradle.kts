plugins {
    `kotlin-dsl`
}

group = "com.ds.studify"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.ksp)
    compileOnly(libs.gradlePlugin.compose)
}

gradlePlugin {
    plugins {
        register("app") {
            id = "studify.app"
            implementationClass = "AppModuleConventionPlugin"
        }
        register("module") {
            id = "studify.module"
            implementationClass = "ModuleDefaultPlugin"
        }
        register("hilt") {
            id = "studify.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("compose.app") {
            id = "studify.compose.app"
            implementationClass = "ComposeAppConventionPlugin"
        }
        register("compose.module") {
            id = "studify.compose.module"
            implementationClass = "ComposeModuleConventionPlugin"
        }
        register("retrofit") {
            id = "studify.retrofit"
            implementationClass = "RetrofitConventionPlugin"
        }
        register("serialization") {
            id = "studify.serialization"
            implementationClass = "SerializationConventionPlugin"
        }
        register("paging") {
            id = "studify.paging"
            implementationClass = "PagingConventionPlugin"
        }
    }
}