import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.ds.studify.convention.androidTestImplementation
import com.ds.studify.convention.debugImplementation
import com.ds.studify.convention.implementation
import com.ds.studify.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposeAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            with(extensions.getByType<BaseAppModuleExtension>()) {
                buildFeatures.compose = true
                composeOptions {
                    kotlinCompilerExtensionVersion =
                        libs.findVersion("composeCompiler").get().requiredVersion
                }
            }

            dependencies {
                implementation(
                    platform(libs.findLibrary("androidx.compose.bom").get()),
                    libs.findBundle("compose").get()
                )
                androidTestImplementation(
                    platform(libs.findLibrary("androidx.compose.bom").get()),
                    libs.findLibrary("androidx.ui.test.junit4").get()
                )
                debugImplementation(
                    libs.findBundle("compose.debug").get()
                )
            }
        }
    }
}