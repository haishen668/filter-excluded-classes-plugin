# Filter Excluded Classes Plugin

## 概述

`Filter Excluded Classes Plugin` 是一个 Gradle 插件，旨在过滤并删除带有 `@ExcludeClass` 注解的类。这使得开发者可以快速剔除需要云端加载的类，优化构建过程。

## 功能

- 自动检测并删除带有 `@ExcludeClass` 注解的类及其内嵌类。
- 将被过滤的类复制到指定的输出目录。

## 安装

在你的项目的 `build.gradle` 文件中添加以下内容：

```groovy
plugins {
    id 'io.github.haishen668.filter-excluded-classes' version '1.0.0'
}
```

确保在 `repositories` 中包含 Maven Central：

```groovy
repositories {
    mavenCentral()
}
```

## 使用方法

### 1. 应用插件

在你的 `build.gradle` 中添加插件：

```groovy
plugins {
    id 'io.github.haishen668.filter-excluded-classes' version '1.0.0'
}
```

### 2. 添加 `@ExcludeClass` 注解

在你希望过滤的类上添加 `@ExcludeClass` 注解。例如：

```java
import io.github.ExcludeClass;

@ExcludeClass
public class MyExcludedClass {
    // 这个类将会被过滤
}
```

### 3. 运行过滤任务

使用以下命令运行过滤任务：

```bash
./gradlew build
```

在构建过程中，插件将会自动执行 `filterExcludedClasses` 任务，过滤掉带有 `@ExcludeClass` 注解的类。

### 4. 查看日志

构建完成后，你可以在 Gradle 日志中查看哪些类被过滤和删除。插件会输出如下信息：

- **Skipping ExcludeClass annotation definition** - 跳过 `ExcludeClass` 注解定义。
- **Excluding class** - 过滤的类名。
- **Copied class to** - 被复制的类的输出路径。
- **Deleted class** - 已删除的类的路径。

## 输出目录

被过滤的类将被复制到项目构建目录的 `libs` 文件夹中。例如：

```
build/libs/
```

## 注意事项

- 确保在编译完成后再运行过滤任务。
- 过滤任务会在 `jar` 打包任务之前执行，确保最终打包不包含被过滤的类。

## 示例项目

可查看[示例项目](https://github.com/haishen668/filter-excluded-classes-plugin)以了解如何在实际项目中使用该插件。

## 许可证

该插件遵循 MIT 许可证。请查看 [LICENSE](LICENSE) 文件以获取更多信息。

## 贡献

欢迎任何贡献！请查阅 [贡献指南](CONTRIBUTING.md) 了解更多信息。
