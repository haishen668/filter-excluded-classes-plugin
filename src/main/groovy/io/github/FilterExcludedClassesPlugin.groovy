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
                def outputDir = project.file("${project.buildDir}/libs")
                if (!inputDir.exists()) {
                    project.logger.warn("编译目录不存在: ${inputDir.absolutePath}")
                    return
                }

                // 确保输出目录存在
                if (!outputDir.exists()) {
                    outputDir.mkdirs()
                }

                inputDir.eachFileRecurse { file ->
                    if (file.name.endsWith('.class')) {
                        // 检查文件名是否是 ExcludeClass 注解的定义
                        if (file.name == 'ExcludeClass.class') {
                            project.logger.lifecycle("Skipping ExcludeClass annotation definition: ${file.name}")
                            return
                        }

                        try {
                            // 读取类文件内容为字符串，使用ISO-8859-1避免字符丢失
                            def classContent = new String(file.bytes, 'ISO-8859-1')

                            // 检查类文件是否包含 "ExcludeClass" 注解
                            if (classContent.contains("ExcludeClass")) {
                                project.logger.lifecycle("Excluding class: ${file.name}")

                                // 将被过滤的类复制到 build/libs/ 目录，不包含目录结构
                                def destFile = new File(outputDir, file.name)
                                destFile.bytes = file.bytes
                                project.logger.lifecycle("Copied class to: ${destFile.absolutePath}")

                                // 删除原始类文件
                                boolean deleted = file.delete()
                                if (deleted) {
                                    project.logger.lifecycle("Deleted class: ${file.absolutePath}")

                                    // 处理内嵌类文件（如 AbstractCommendExecutor$1.class）
                                    def parentDir = file.parentFile
                                    def baseName = file.name.replace('.class', '')
                                    parentDir.eachFileMatch(~/${baseName}\$.+\.class/) { innerFile ->

                                        // 将内嵌类复制到 build/libs/ 目录
                                        def innerDestFile = new File(outputDir, innerFile.name)
                                        innerDestFile.bytes = innerFile.bytes
                                        project.logger.lifecycle("Copied inner class to: ${innerDestFile.absolutePath}")

                                        // 删除内嵌类文件
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
