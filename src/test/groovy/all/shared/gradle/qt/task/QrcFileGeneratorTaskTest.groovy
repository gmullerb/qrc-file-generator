//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.qt.QrcFileGeneratorConfig
import all.shared.gradle.testfixtures.SpyProjectFactory

import groovy.transform.CompileStatic

import org.gradle.api.Project

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNull

@CompileStatic
class QrcFileGeneratorTaskTest {
  @Test
  void shouldAdjustConfiguration() {
    final QrcFileGeneratorConfig extraConfig = new QrcFileGeneratorConfig() {}
    final Project spyProject = SpyProjectFactory.build()
    final QrcFileGeneratorTask task = spyProject.tasks.create('testTask', QrcFileGeneratorTask)
    extraConfig.sourceFolder = 'source1'
    extraConfig.sourceExtensions = ['ext1', 'ext2']
    extraConfig.destinationFile = 'dest1.ext'
    extraConfig.destinationFolder = 'dest1'
    extraConfig.excludes = ['exclude1', 'exclude2']
    extraConfig.rccVersion = '1.1'
    extraConfig.rccPrefix = 'prefix1'

    task.adjustConfiguration(extraConfig)

    assertEquals('source1', task.sourceFolder)
    assertEquals(['ext1', 'ext2'], task.sourceExtensions)
    assertEquals('dest1.ext', task.destinationFile)
    assertEquals('dest1', task.destinationFolder)
    assertEquals(['exclude1', 'exclude2'], task.excludes)
    assertEquals('1.1', task.rccVersion)
    assertEquals('prefix1', task.rccPrefix)
  }

  @Test
  void shouldAdjustConfigurationWhenExtensionIsInvalid() {
    final QrcFileGeneratorConfig extraConfig = new QrcFileGeneratorConfig() {}
    final Project spyProject = SpyProjectFactory.build()
    final QrcFileGeneratorTask task = spyProject.tasks.create('testTask', QrcFileGeneratorTask)

    task.adjustConfiguration(extraConfig)

    assertEquals('', task.sourceFolder)
    assertEquals([], task.sourceExtensions)
    assertEquals('qml.qrc', task.destinationFile)
    assertEquals('', task.destinationFolder)
    assertEquals([], task.excludes)
    assertEquals('1.0', task.rccVersion)
    assertNull(task.rccPrefix)
  }

  @Test
  void shouldNotAdjustConfiguration() {
    final QrcFileGeneratorConfig extraConfig = new QrcFileGeneratorConfig() {}
    final Project spyProject = SpyProjectFactory.build()
    final QrcFileGeneratorTask task = spyProject.tasks.create('testTask', QrcFileGeneratorTask)
    task.sourceFolder = 'source0'
    task.sourceExtensions = ['ext0', 'ext1']
    task.destinationFile = 'dest0.ext'
    task.destinationFolder = 'dest0'
    task.excludes = ['exclude0', 'exclude1']
    task.rccVersion = '0.1'
    task.rccPrefix = 'prefix0'
    extraConfig.sourceFolder = 'source1'
    extraConfig.sourceExtensions = ['ext1', 'ext2']
    extraConfig.destinationFile = 'dest1.ext'
    extraConfig.destinationFolder = 'dest1'
    extraConfig.excludes = ['exclude1', 'exclude2']
    extraConfig.rccVersion = '1.1'
    extraConfig.rccPrefix = 'prefix1'

    task.adjustConfiguration(extraConfig)

    assertEquals('source0', task.sourceFolder)
    assertEquals(['ext0', 'ext1'], task.sourceExtensions)
    assertEquals('dest0.ext', task.destinationFile)
    assertEquals('dest0', task.destinationFolder)
    assertEquals(['exclude0', 'exclude1'], task.excludes)
    assertEquals('0.1', task.rccVersion)
    assertEquals('prefix0', task.rccPrefix)
  }
}
