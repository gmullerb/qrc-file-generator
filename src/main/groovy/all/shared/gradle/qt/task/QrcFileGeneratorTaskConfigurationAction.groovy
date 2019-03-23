//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.file.FileListerExtension
import all.shared.gradle.qt.QrcFileGeneratorConfig

import groovy.transform.CompileStatic

import org.gradle.api.Action
import org.gradle.api.Task

@CompileStatic
class QrcFileGeneratorTaskConfigurationAction implements Action<Task> {
  private final QrcFileGeneratorConfig config
  private final FileListerExtension fileLister

  QrcFileGeneratorTaskConfigurationAction(
      final QrcFileGeneratorConfig config,
      final FileListerExtension fileLister) {
    this.config = config
    this.fileLister = fileLister
  }

  void execute(final Task task) {
    task.description = 'Creates QRC file from specified information'
    task.group = 'Build'
    task.doFirst((Action) new QrcFileGeneratorTaskAction(config, fileLister))
  }
}
