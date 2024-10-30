// buildSrc/src/main/groovy/io/github/FilterExcludedClassesPlugin.groovy
package io.github

import org.gradle.api.Plugin
import org.gradle.api.Project

class FilterExcludedClassesPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.tasks.register('filterExcludedClasses') {
            group = 'Filtering'
            description = '过滤并删除带有 @ExcludeClass 注解的类及其内嵌类'

            doLast {
                def inputDir = project.file("${project.buildDir}/classes/java/main")
                if (!inputDir.exists()) {
                    project.logger.warn("编译目录不存在: ${inputDir.absolutePath}")
                    return
                }

                inputDir.eachFileRecurse { file ->
                    if (file.name.endsWith('.class')) {
                        // 检查文件名是否是 ExcludeClass 注解的定义
                        if (file.name == 'ExcludeClass.class') {
                            project.logger.lifecycle("Skipping ExcludeClass annotation definition: ${file.name}")
                            return
                        }

                        project.logger.lifecycle("Processing file: ${file.absolutePath}")
                        try {
                            // 读取类文件内容为字符串，使用ISO-8859-1避免字符丢失
                            def classContent = new String(file.bytes, 'ISO-8859-1')

                            // 检查类文件是否包含 "ExcludeClass" 注解
                            if (classContent.contains("ExcludeClass")) {
                                project.logger.lifecycle("Excluding class: ${file.name}")
                                boolean deleted = file.delete()
                                if (deleted) {
                                    project.logger.lifecycle("Deleted class: ${file.absolutePath}")

                                    // 删除所有内嵌类文件（如 AbstractCommendExecutor$1.class）
                                    def parentDir = file.parentFile
                                    def baseName = file.name.replace('.class', '')
                                    parentDir.eachFileMatch(~/${baseName}\$.+\.class/) { innerFile ->
                                        project.logger.lifecycle("Deleting inner class: ${innerFile.absolutePath}")
                                        boolean innerDeleted = innerFile.delete()
                                        if (innerDeleted) {
                                            project.logger.lifecycle("Deleted inner class: ${innerFile.absolutePath}")
                                        } else {
                                            project.logger.warn("Failed to delete inner class: ${innerFile.absolutePath}")
                                        }
                                    }
                                } else {
                                    project.logger.warn("Failed to delete class: ${file.absolutePath}")
                                }
                            } else {
                                project.logger.lifecycle("Including class: ${file.name}")
                            }
                        } catch (Exception e) {
                            project.logger.error("Error processing file: ${file.absolutePath}", e)
                        }
                    }
                }
            }
        }

        // 确保在打包前执行过滤任务
        project.tasks.named('jar') {
            it.dependsOn('filterExcludedClasses')
        }
    }
}
