//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.file.FileListerExtension
import all.shared.gradle.qt.QrcFileGeneratorConfig

import groovy.transform.CompileStatic

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree

@CompileStatic
class QrcFileGeneratorTaskAction implements Action<QrcFileGeneratorTask> {
  private final QrcFileGeneratorConfig config
  private final FileListerExtension fileLister

  public QrcFileGeneratorTaskAction(
      final QrcFileGeneratorConfig config,
      final FileListerExtension fileLister) {
    this.config = config
    this.fileLister = fileLister
  }

  public void execute(final QrcFileGeneratorTask thisTask) {
    thisTask.adjustConfiguration(config)
    generate(thisTask.project, thisTask)
  }

  protected final void generate(final Project inProject, final QrcFileGeneratorConfig fromConfig) {
    try {
      final QrcBodyGenerator qrcBodyGenerator = new QrcBodyGenerator(fromConfig)
      final File qrcFile = inProject.file(qrcBodyGenerator.calculateFilePath())
      qrcFile.text = qrcBodyGenerator.createQrcBody(obtainResourceFiles(fromConfig))
    }
    catch (any) {
      inProject.logger.error('qrc-file-generator could not create qrc file, check task or extension configuration: {}', fromConfig)
    }
  }

  protected final ConfigurableFileTree obtainResourceFiles(final QrcFileGeneratorConfig fromConfig) {
    fileLister.obtainPartialFileTree(
      fromConfig.sourceFolder,
      [
        includes: fromConfig.sourceExtensions.collect { "**/*.$it" },
        excludes: fromConfig.excludes
      ]
    )
  }
}
