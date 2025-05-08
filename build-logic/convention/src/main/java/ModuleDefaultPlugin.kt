import com.android.build.gradle.LibraryExtension
import com.ds.studify.convention.ProjectConfiguration
import com.ds.studify.convention.androidTestImplementation
import com.ds.studify.convention.libs
import com.ds.studify.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class ModuleDefaultPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = ProjectConfiguration.COMPILE_SDK

                with(defaultConfig) {
                    targetSdk = ProjectConfiguration.TARGET_SDK
                    minSdk = ProjectConfiguration.MIN_SDK
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = ProjectConfiguration.SOURCE_COMPATIBILITY
                    targetCompatibility = ProjectConfiguration.TARGET_COMPATIBILITY
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }

            configure<KotlinAndroidProjectExtension> {
                compilerOptions.apply {
                    jvmTarget.set(ProjectConfiguration.KOTLIN_COMPILER_JVM_TARGET)
                }
            }

            dependencies {
                testImplementation(
                    libs.findLibrary("junit").get(),
                    libs.findLibrary("kotlinx.coroutines.test").get()
                )
                androidTestImplementation(libs.findLibrary("androidx.junit").get())
                androidTestImplementation(libs.findLibrary("androidx.espresso.core").get())
            }
        }
    }
}