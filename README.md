
# Filter Excluded Classes Plugin

## 概述 / Overview

`Filter Excluded Classes Plugin` 是一个 Gradle 插件，旨在过滤并删除带有 `@ExcludeClass` 注解的类。这使得开发者可以快速剔除需要云端加载的类，优化构建过程。  
`Filter Excluded Classes Plugin` is a Gradle plugin designed to filter and remove classes annotated with `@ExcludeClass`. This allows developers to quickly eliminate classes that need to be loaded from the cloud, optimizing the build process.

## 功能 / Features

- 自动检测并删除带有 `@ExcludeClass` 注解的类及其内嵌类。  
  Automatically detects and removes classes and their nested classes annotated with `@ExcludeClass`.
- 将被过滤的类复制到指定的输出目录。  
  Copies the filtered classes to a specified output directory.

## 安装 / Installation

在你的项目的 `build.gradle` 文件中添加以下内容：  
Add the following to your project's `build.gradle` file:

```groovy
plugins {
    id 'io.github.haishen668.filter-excluded-classes' version '1.0.0'
}
```

确保在 `repositories` 中包含 Maven Central：  
Ensure that Maven Central is included in `repositories`:

```groovy
repositories {
    mavenCentral()
}
```

## 使用方法 / Usage

### 1. 应用插件 / Apply the Plugin

在你的 `build.gradle` 中添加插件：  
Add the plugin in your `build.gradle`:

```groovy
plugins {
    id 'io.github.haishen668.filter-excluded-classes' version '1.0.0'
}
```

### 2. 添加 `@ExcludeClass` 注解 / Add `@ExcludeClass` Annotation

在你希望过滤的类上添加 `@ExcludeClass` 注解。例如：  
Add the `@ExcludeClass` annotation to the classes you want to filter. For example:

```java
import io.github.ExcludeClass;

@ExcludeClass
public class MyExcludedClass {
    // 这个类将会被过滤  
    // This class will be filtered
}
```

### 3. 运行过滤任务 / Run the Filtering Task

使用以下命令运行过滤任务：  
Run the filtering task using the following command:

```bash
./gradlew build
```

在构建过程中，插件将会自动执行 `filterExcludedClasses` 任务，过滤掉带有 `@ExcludeClass` 注解的类。  
During the build process, the plugin will automatically execute the `filterExcludedClasses` task to filter out classes annotated with `@ExcludeClass`.

### 4. 查看日志 / View Logs

构建完成后，你可以在 Gradle 日志中查看哪些类被过滤和删除。插件会输出如下信息：  
After the build is complete, you can check the Gradle logs to see which classes were filtered and deleted. The plugin will output the following information:

- **Skipping ExcludeClass annotation definition** - 跳过 `ExcludeClass` 注解定义。
- **Excluding class** - 过滤的类名。
- **Copied class to** - 被复制的类的输出路径。
- **Deleted class** - 已删除的类的路径。

## 输出目录 / Output Directory

被过滤的类将被复制到项目构建目录的 `libs` 文件夹中。例如：  
Filtered classes will be copied to the `libs` folder in the project build directory. For example:

```
build/libs/
```

## 注意事项 / Notes

- 确保在编译完成后再运行过滤任务。
- Ensure to run the filtering task after the compilation is complete.
- 过滤任务会在 `jar` 打包任务之前执行，确保最终打包不包含被过滤的类。
- The filtering task will execute before the `jar` packaging task, ensuring that the final package does not include filtered classes.

## 示例项目 / Example Project

可查看[示例项目](https://github.com/haishen668/filter-excluded-classes-plugin)以了解如何在实际项目中使用该插件。  
You can view the [example project](https://github.com/haishen668/filter-excluded-classes-plugin) to see how to use the plugin in a real project.

## 许可证 / License

该插件遵循 MIT 许可证。请查看 [LICENSE](LICENSE) 文件以获取更多信息。  
This plugin is licensed under the MIT License. Please refer to the [LICENSE](LICENSE) file for more information.

## 贡献 / Contributing

欢迎任何贡献！请查阅 [贡献指南](CONTRIBUTING.md) 了解更多信息。  
Contributions are welcome! Please refer to the [Contributing Guide](CONTRIBUTING.md) for more information.
