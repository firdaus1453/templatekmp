package com.template.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    applicationExtension: ApplicationExtension
) {
    applicationExtension.apply {
        compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

        namespace = libs.findVersion("projectApplicationId").get().toString() +
            path.removePrefix(":").replace(":", ".").let { if (it.isNotEmpty()) ".$it" else "" }

        defaultConfig {
            minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }
    }

    dependencies.apply {
        add("coreLibraryDesugaring", libs.findLibrary("android-desugarJdkLibs").get())
    }
}
