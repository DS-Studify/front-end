import com.ds.studify.convention.implementation
import com.ds.studify.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("kotlinx-serialization")
            }

            dependencies {
                implementation(libs.findLibrary("kotlinx-serialization").get())
            }
        }
    }
}