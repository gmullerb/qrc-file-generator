//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.qt.QrcFileGeneratorConfig

import groovy.transform.CompileStatic

import org.gradle.api.DefaultTask

@CompileStatic
class QrcFileGeneratorTask extends DefaultTask implements QrcFileGeneratorConfig {
  private void adjustDestinationFile(final String destinationFile) {
    if (this.destinationFile == null) {
      this.destinationFile = destinationFile ?: 'qml.qrc'
    }
  }

  private void adjustSourceExtensions(final List<String> sourceExtensions) {
    if (this.sourceExtensions == null) {
      this.sourceExtensions = sourceExtensions ?: [] as List<String>
    }
  }

  private void adjustSourceFolder(final String sourceFolder) {
    if (this.sourceFolder == null) {
      this.sourceFolder = sourceFolder ?: ''
    }
  }

  private void adjustDestinationFolder(final String destinationFolder) {
    if (this.destinationFolder == null) {
      this.destinationFolder = destinationFolder ?: ''
    }
  }

  private void adjustExcludes(final List<String> excludes) {
    if (this.excludes == null) {
      this.excludes = excludes ?: [] as List<String>
    }
  }

  private void adjustRccVersion(final String rccVersion) {
    if (this.rccVersion == null) {
      this.rccVersion = rccVersion ?: '1.0'
    }
  }

  private void adjustRccPrefix(final String rccPrefix) {
    if (this.rccPrefix == null) {
      this.rccPrefix = rccPrefix
    }
  }

  public void adjustConfiguration(final QrcFileGeneratorConfig config) {
    adjustSourceFolder(config.sourceFolder)
    adjustSourceExtensions(config.sourceExtensions)
    adjustDestinationFolder(config.destinationFolder)
    adjustDestinationFile(config.destinationFile)
    adjustExcludes(config.excludes)
    adjustRccVersion(config.rccVersion)
    adjustRccPrefix(config.rccPrefix)
  }
}
