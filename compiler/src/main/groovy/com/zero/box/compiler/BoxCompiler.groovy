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

        def android = project.getExtensions().getByType(AppExtension)

        println(android.applicationVariants)
        project.extensions.create("BoxConfig",BoxConfig)
        project.tasks.whenTaskAdded {
            task ->
                Task t = task
                if (t.name.endsWith("generateDebugBuildConfig") || t.name.endsWith("generateReleaseBuildConfig")) {
                    println(t)
                    t.doLast {
                        it.outputs.files.each { File file ->
                            RuntimeGenerator generator = new RuntimeGenerator();
                            if (project.BoxConfig.applicationId!=null){
                                generator.generateDefault( project.BoxConfig.applicationId, file)
                            }
                        }
                    }
                }

        }
    }
}