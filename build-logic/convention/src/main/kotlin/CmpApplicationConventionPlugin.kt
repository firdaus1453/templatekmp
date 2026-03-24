import com.template.convention.configureIosTargets
import com.template.convention.configureDesktopTarget
import com.template.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.ComposeExtension

class CmpApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("template.cmp.library")
            }

            configureIosTargets()
            configureDesktopTarget()

            val compose = extensions.getByType(ComposeExtension::class.java).dependencies

            dependencies {
                "commonMainImplementation"(compose.components.resources)
            }
        }
    }
}
