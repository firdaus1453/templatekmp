import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KoverConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            
            // WORKAROUND for AGP 9 com.android.kotlin.multiplatform.library vs Kover 0.9.1
            // Kover expects an "android" extension with variants, else it crashes.
            // By supplying an empty list of variants, Kover skips Android instrumentation 
            // but still runs successfully for KMP standard targets (like desktop/JVM).
            if (extensions.findByName("android") == null) {
                extensions.add("android", object {
                    val libraryVariants: Collection<Any> = emptyList()
                    val applicationVariants: Collection<Any> = emptyList()
                    val unitTestVariants: Collection<Any> = emptyList()
                    val testVariants: Collection<Any> = emptyList()
                    val buildTypes: Collection<Any> = emptyList()
                    val productFlavors: Collection<Any> = emptyList()
                    val defaultConfig = object {
                        val missingDimensionStrategies: Map<String, Any> = emptyMap()
                    }
                })
            }

            pluginManager.apply("org.jetbrains.kotlinx.kover")

            extensions.configure<kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension> {
                reports {
                    filters {
                        excludes {
                            classes(
                                "*_Factory",
                                "*_HiltModules*",
                                "*BuildKonfig*",
                                "*ComposableSingletons*",
                                "*.di.*Module*",
                                "*_Impl",
                                "*_Impl\$*",
                            )
                            packages(
                                "*.di",
                                "*.theme",
                                "*.designsystem",
                            )
                        }
                    }

                    verify {
                        rule("Minimum coverage") {
                            minBound(0)
                        }
                    }
                }
            }
        }
    }
}
