//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.qt.QrcFileGeneratorConfig

import groovy.transform.CompileStatic

import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path

import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileVisitDetails

@CompileStatic
class QrcBodyGenerator {
  private final FileSystem fileSystem = FileSystems.default
  private final QrcFileGeneratorConfig config

  public QrcBodyGenerator(final QrcFileGeneratorConfig config) {
    this.config = config
  }

  public final Path calculateFilePath() {
    return fileSystem.getPath(config.destinationFolder, config.destinationFile)
  }

  public final String createQrcBody(final ConfigurableFileTree fileTree) {
    final StringBuilder body = new StringBuilder("<!DOCTYPE RCC>\n<RCC version=\"$config.rccVersion\">\n")
    body << "  <qresource${calculatePrefix()}>\n"
    final String folder = calculateFilesFolder()
    fileTree.visit { final FileVisitDetails fileInfo ->
      if (!fileInfo.directory) {
        body << "    <file>${folder}${fileInfo.path}</file>\n"
      }
    }
    body << '  </qresource>\n</RCC>\n'
    return body.toString()
  }

  protected final String calculatePrefix() {
    config.rccPrefix
      ? " prefix=\"${config.rccPrefix}\""
      : ''
  }

  protected final String calculateFilesFolder() {
    config.sourceFolder == config.destinationFolder
     ? ''
     : "${relativizeFolders()}/"
  }

  protected final String relativizeFolders() {
    final Path sourcePath = fileSystem.getPath(config.sourceFolder)
    final Path destinationPath = fileSystem.getPath(config.destinationFolder)

    return destinationPath
      .relativize(sourcePath)
      .toString()
  }
}
