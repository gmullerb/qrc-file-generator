# QRC File Generation plugin

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](/LICENSE.txt) 
[![Download](https://api.bintray.com/packages/gmullerb/all.shared.gradle/qrc-file-generator/images/download.svg)](https://bintray.com/gmullerb/all.shared.gradle/qrc-file-generator/_latestVersion) 
[![coverage report](https://gitlab.com/gmullerb/qrc-file-generator/badges/master/coverage.svg)](https://gitlab.com/gmullerb/qrc-file-generator/commits/master)

**A Gradle plugin for generating QRC File for QML Projects.**

This project is licensed under the terms of the [MIT license](/LICENSE.txt).
__________________

## Quick start

1 . Apply the plugin:

```gradle
 plugins {
   id 'all.shared.gradle.qrc-file-generator' version '1.0.0'
 }
```

2 . Add the following line to the QT/QML project `.pro` file:

* This will point the QML Project build to the right spot, i.e. pointing to the QRC's Generated File.

```JS
  RESOURCES += 'src/main/resources/qml.qrc'
```

3 . Execute `generateQrcFile` task.

* This will generate qrc file based on default values:
  * inputs will be all the `.qml`, `.js`, `.mjs`, `.conf`, `.png`, `.jpg` and `.ttf` files located in folder `src/main/resources`.
  * output will be generated in `src/main/resources` folder, with name `qml.qrc`.

4 . Jump to [Using/Configuration](#Using/Configuration), for customization or digging on How it works.
__________________

## Goal

The automatic generation of the `.qrc` file required when working with [QML](https://doc.qt.io/qt-5/qmlreference.html). It is frequent to miss to update the `.qrc` file, when for example add a new file, changing a file name, changing a file location, etc. This plugin will allow to never worry about that.

## Features

* Offers a unique task, `generateQrcFile`, which automatically generates the required `.qrc` file based on default values.
* Allows for customization of input and output values.
* Applies the required plugins to the project:
  * [File Lister plugin](https://github.com/gmullerb/file-lister).
__________________

## Using/Configuration

### Prerequisites

* None

### Gradle configuration

1. Apply the plugin:

```gradle
 plugins {
   id 'all.shared.gradle.qrc-file-generator' version '1.0.0'
 }
```

### Options

The following options are available for customization:

* **`sourceExtensions`**`: string[]`: sets extensions to look for, that define the [QML resources](https://doc.qt.io/qt-5/resources.html).
  * default: `['qml', 'js', 'mjs', 'conf', 'png', 'jpg', 'ttf']`
  * These are internally processed using [File lister plugin](https://github.com/gmullerb/file-lister).
* **`sourceFolder`**`: string`: specifies the path, relative to the root of the project, to look for `sourceExtensions` files.
  * default: `'src/main/resources'`
  * **should not end with `/`**.
* **`destinationFile`**`: string`: specifies the file name.
  * default: `'qml.qrc'`
* **`destinationFolder`**`: string`: sets the path, relative to the root of the project, where `destinationFile` will be created.
  * default: `'src/main/resources'`
  * **included resource files must be located in same folder where `.qrc` file is located or a subfolder** (RCC requirement).
  * **should not end with `/`**.
* **`excludes`**`: string[]`: specifies ANT patterns for exclusions.
  * default: `[]`
  * These are internally processed using [File lister plugin](https://github.com/gmullerb/file-lister).
* **`rccVersion`**`: string`: sets [RCC](https://doc.qt.io/qt-5/rcc.html) version.
  * default: `'1.0'`
* **`rccPrefix`**`: string`: sets [RCC](https://doc.qt.io/qt-5/rcc.html) prefix.
  * default: `'/'`

Basically `generateQrcFile` task will look for resources based on `sourceExtensions` in `sourceFolder` and subfolders, excluding any pattern set in `excludes`, and then will create `destinationFile` in the `destinationFolder` with `rccVersion` and `rccPrefix`.

* **Resulting paths** inside `destinationFile` will be relative to the directory containing the `destinationFile` file.

E.g.:

Given the following folder structure, and using default values:

```
  src/
    main/
      cpp/
        main.cpp
      resources/
        main.qml
        folderA/
          someComponent.qml
          someIcon.png
        folderB/
          otherComponent.qml
          folderB1/
            some.mjs
        fonts/
          fontA.ttf
```

will generate the xml file, `src/main/resources/qml.qrc`:

```xml
  <!DOCTYPE RCC>
  <RCC version="1.0">
    <qresource prefix="/">
      <file>main.qml</file>
      <file>folderA/someComponent.qml</file>
      <file>folderA/someIcon.png</file>
      <file>folderB/otherComponent.qml</file>
      <file>folderB/folderB1/some.mjs</file>
      <file>fonts/fontA.ttf</file>
    </qresource>
  </RCC>
```

### Customization

Customization can be done using plugin options:

```gradle
  qrcFileGenerator {
    sourceFolder = ..
    sourceExtensions = ..
    destinationFile = ..
    destinationFolder = ..
    excludes = ..
    rccVersion = ..
    rccPrefix = ..
  }
```

Customization can be done using task options:

```gradle
  generateQrcFile {
    sourceFolder = ..
    sourceExtensions = ..
    destinationFile = ..
    destinationFolder = ..
    excludes = ..
    rccVersion = ..
    rccPrefix = ..
  }
```

E.G.:

Setting new Extensions:

```gradle
  qrcFileGenerator {
    sourceExtensions = ['qml', 'js', 'svg']
  }
```

Adding Extensions:

```gradle
  qrcFileGenerator {
    sourceExtensions += 'svg'
  }
```

Finally, It's useful to set a dependency on `generateQrcFile` for the task that calls the `qmake` command, e.g.:

```gradle
  someQmakeTask {
    dependsOn 'generateQrcFile'
  }
```

* It is not necessary to include qml.qrc file in the control version repository, so can be added to the `.gitignore` file:

```gitignore
  qml.qrc
```
__________________

## Extending/Developing

### Prerequisites

* [Java](http://www.oracle.com/technetwork/java/javase/downloads).
* [Git](https://git-scm.com/downloads) (only if you are going to clone the project).

### Getting it

Clone or download the project[1], in the desired folder execute:

```sh
git clone https://github.com/gmullerb/qrc-file-generator
```

> [1] [Cloning a repository](https://help.github.com/articles/cloning-a-repository/)

### Set up

* **No need**, only download and run (It's Gradle! Yes!).

#### Make it your own

  1. Remove the Git Origin: `git remote remove origin`.
  2. Add your Git origin: `git remote add origin https://gitlab.com/yourUser/yourRepo.git`.
  3. Remove the License for 'All rights reserved' projects, or Modify the License for your needs.
  4. Change the `all.shared.gradle.qrc-file-generator.properties` file name to your plugin Id, e.g. `some.pluginId.properties`.

### Building it

* To build it:
  * `gradlew`: this will run default task, or
  * `gradlew build`.

* To assess files:
  * `gradlew assessCommon`: will check common style of files.
  * `gradlew assessGradle`: will check code style of Gradle's.
  * `gradlew codenarcMain`: will check code style of Groovy's source files.
  * `gradlew codenarcTest`: will check code style of Groovy's test files.
  * `assemble` task depends on these four tasks.

* To test code: `gradlew test`
  * This will run jacoco code coverage [1].

* To publish plugin: `./gradlew -PPLUGIN_ID=some.pluginId publishPlugins`
  * `-PPLUGIN_ID` indicates the plugin id.

* To get all the tasks for the project: `gradlew tasks --all`

| [1] May not get 100% since Groovy adds some extra code, that may not be tested.

### Folders structure

```
  /src
    /main
      /groovy
    /test
      /groovy
```

- `src/main/groovy`: Source code files.
  - [`QrcFileGenerator`](src/main/groovy/all/shared/gradle/qt/QrcFileGenerator.groovy) is where all the plugin's magic happens.
  - [`QrcBodyGenerator`](src/main/groovy/all/shared/gradle/qt/task/QrcBodyGenerator.groovy) is where all the generation's magic happens.
- `src/test/groovy`: Test code files[1].

> [1] Tests are done using [JUnit](http://junit.org), [Mockito](http://javadoc.io/page/org.mockito/mockito-core/latest/org/mockito/Mockito.html) and [Spy Project Factory](https://github.com/gmullerb/spy-project-factory).

### Convention over Configuration

All `all.shared.gradle` plugins define:

* _PluginName_**Plugin**: which contains the class implements `Plugin` interface.
* _PluginName_**Extension**: which represent the extension of the plugin.
* If Tasks are define, then their names will be _TaskName_**Task**.
* If Actions are define, then their names will be _ActionName_**Action**.

All `all.shared.gradle` plugins have two **`static`** members:

* `String EXTENSION_NAME`: This will have the name of the extension that the plugin add.
  * if the plugin does not add an extension the this field will not exist.

* `String TASK_NAME`: This will have the name of the **unique** task that the plugin add.
  * if the plugin does not add a task or add more than one task, then this field will not exist.

* `boolean complement(final ..)`: will apply the plugin and return true if successful, false otherwise.
  * this methods is **exactly equivalent to the instance `apply` method**, but without instantiate the class if not required.

Both may be useful when applying the plugin when creating custom plugins.

All `all.shared.gradle` plugins "silently" fail when the extension can not be added.

## Documentation

* [`CHANGELOG.md`](CHANGELOG.md): add information of notable changes for each version here, chronologically ordered [1].

> [1] [Keep a Changelog](http://keepachangelog.com)

## License

[MIT License](/LICENSE.txt)
__________________

## Remember

* Use code style verification tools => Encourages Best Practices, Efficiency, Readability and Learnability.
* Start testing early => Encourages Reliability and Maintainability.
* Code Review everything => Encourages Functional suitability, Performance Efficiency and Teamwork.

## Additional words

Don't forget:

* **Love what you do**.
* **Learn everyday**.
* **Learn yourself**.
* **Share your knowledge**.
* **Learn from the past, dream on the future, live and enjoy the present to the max!**.

At life:

* Let's act, not complain.
* Be flexible.

At work:

* Let's give solutions, not questions.
* Aim to simplicity not intellectualism.
