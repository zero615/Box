package com.zero.box.compiler


import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.google.auto.service.AutoService
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import javax.annotation.Nonnull

@AutoService
class BoxCompiler implements Plugin<Project> {


    @Override
    void apply(@Nonnull Project project) {
        if (project.plugins.hasPlugin(AppPlugin)) {
            def android = project.getExtensions().getByType(AppExtension)

            println(android.applicationVariants)
            project.tasks.whenTaskAdded {
                task ->
                    Task t = task
                    if (t.name.endsWith("generateDebugBuildConfig") || t.name.endsWith("generateReleaseBuildConfig")) {
                        println(t)
                        t.doLast {
                            it.outputs.files.each { File file ->
                                RuntimeGenerator generator = new RuntimeGenerator();
                                generator.generateDefault(android.defaultConfig.applicationId, file)
                            }
                        }
                    }

            }
        }
    }
}