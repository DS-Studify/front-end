import com.ds.studify.convention.implementation
import com.ds.studify.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("studify.serialization")
            }

            dependencies {
                implementation(libs.findBundle("retrofit").get())
            }
        }
    }
}