//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt

import all.shared.gradle.file.FileListerExtension
import all.shared.gradle.file.FileListerPlugin
import all.shared.gradle.qt.task.QrcFileGeneratorTask
import all.shared.gradle.qt.task.QrcFileGeneratorTaskConfigurationAction

import groovy.transform.CompileStatic

import org.gradle.api.Project

@CompileStatic
class QrcFileGenerator {
  private final Project project

  public QrcFileGenerator(final Project project) {
    this.project = project
  }

  public boolean complement(final String extensionName, final String taskName) {
    final QrcFileGeneratorConfig extension = addExtension(extensionName)
    if (extension) {
      addRequiredPlugin()
      return addTask(taskName, extension)
    }
    return false
  }

  protected final QrcFileGeneratorConfig createExtension() {
    final QrcFileGeneratorConfig extension = new QrcFileGeneratorConfig() {}
    extension.sourceFolder = 'src/main/resources'
    extension.sourceExtensions = ['qml', 'js', 'mjs', 'conf', 'png', 'jpg', 'ttf']
    extension.destinationFile = 'qml.qrc'
    extension.destinationFolder = 'src/main/resources'
    extension.excludes = []
    extension.rccVersion = '1.0'
    extension.rccPrefix = '/'
    return extension
  }

  protected final QrcFileGeneratorConfig addExtension(final String extensionName) {
    if (project.extensions.findByName(extensionName) == null) {
      final QrcFileGeneratorConfig extension = createExtension()
      project.extensions.add(extensionName, extension)
      project.logger.debug('Added qrc-file-generator extension: {}', extensionName)
      return extension
    }
    project.logger.error('Couldn\'t add qrc-file-generator extension {}', extensionName)
    return null
  }

  protected final boolean addTask(final String taskName, final QrcFileGeneratorConfig extension) {
    if (!extension
        || project.tasks.findByPath(taskName) != null
        || !addFileGeneratorTask(taskName, extension)) {
      project.logger.error('Couldn\'t add qrc-file-generator task: {}', taskName)
      return false
    }
    return true
  }

  private boolean addFileGeneratorTask(final String taskName, final QrcFileGeneratorConfig extension) {
    final FileListerExtension fileLister = (FileListerExtension)project.extensions.findByName(FileListerPlugin.EXTENSION_NAME)
    if (fileLister != null) {
      project.tasks.create(
        taskName,
        QrcFileGeneratorTask,
        new QrcFileGeneratorTaskConfigurationAction(extension, fileLister)
      )
      project.logger.debug('Added qrc-file-generator task: {}', taskName)
      return true
    }
    return false
  }

  protected final void addRequiredPlugin() {
    if (project.extensions.findByName(FileListerPlugin.EXTENSION_NAME) == null) {
      new FileListerPlugin()
        .apply(project)
      project.logger.debug('qrc-file-generator extension applied {} plugin', FileListerPlugin)
    }
  }
}
