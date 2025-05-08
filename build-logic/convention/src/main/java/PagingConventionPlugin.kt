import com.ds.studify.convention.implementation
import com.ds.studify.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class PagingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(
                    libs.findLibrary("paging.runtime").get(),
                    libs.findLibrary("paging.compose").get()
                )
            }
        }
    }
}