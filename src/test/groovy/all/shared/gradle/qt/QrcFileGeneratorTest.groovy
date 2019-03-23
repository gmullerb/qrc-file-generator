//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt

import all.shared.gradle.file.FileListerExtension
import all.shared.gradle.file.FileListerPlugin
import all.shared.gradle.testfixtures.SpyProjectFactory

import groovy.transform.CompileStatic

import org.gradle.api.Project

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertTrue

import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify

@CompileStatic
class QrcFileGeneratorTest {
  private final Project spyProject = SpyProjectFactory.build()
  private final QrcFileGenerator qrcFileGenerator = new QrcFileGenerator(spyProject)

  @Test
  void shouldComplement() {
    final boolean result = qrcFileGenerator.complement('qrcFileGeneratorConfig', 'qrcFileGeneratorTask')

    assertTrue(result)
  }

  @Test
  void shouldNotComplement() {
    spyProject.extensions.add('qrcFileGeneratorConfig', 'someValue')

    final boolean result = qrcFileGenerator.complement('qrcFileGeneratorConfig', 'qrcFileGeneratorTask')

    assertFalse(result)
  }

  @Test
  void shouldCreateExtension() {
    final QrcFileGeneratorConfig extension = qrcFileGenerator.createExtension()

    assertEquals('src/main/resources', extension.sourceFolder)
    assertEquals(['qml', 'js', 'mjs', 'conf', 'png', 'jpg', 'ttf'], extension.sourceExtensions)
    assertEquals('qml.qrc', extension.destinationFile)
    assertEquals('src/main/resources', extension.destinationFolder)
    assertEquals([], extension.excludes)
    assertEquals('1.0', extension.rccVersion)
    assertEquals('/', extension.rccPrefix)
  }

  @Test
  void shouldAddExtension() {
    final QrcFileGeneratorConfig extension = qrcFileGenerator.addExtension('qrcFileGeneratorConfig')

    assertNotNull(extension)
    assertTrue(spyProject.properties['qrcFileGeneratorConfig'] instanceof QrcFileGeneratorConfig)
  }

  @Test
  void shouldNotAddExtensionWhenExtensionNameNotAvailable() {
    spyProject.extensions.add('qrcFileGeneratorConfig', 'someValue')

    final QrcFileGeneratorConfig extension = qrcFileGenerator.addExtension('qrcFileGeneratorConfig')

    assertNull(extension)
    verify(spyProject.logger)
      .error(eq('Couldn\'t add qrc-file-generator extension {}'), eq('qrcFileGeneratorConfig'))
  }

  @Test
  void shouldAddTask() {
    spyProject.extensions.add(FileListerPlugin.EXTENSION_NAME, mock(FileListerExtension))

    final boolean result = qrcFileGenerator.addTask('qrcFileGeneratorTask', new QrcFileGeneratorConfig() {})

    assertTrue(result)
    assertNotNull(spyProject.tasks.findByPath(':qrcFileGeneratorTask'))
    verify(spyProject.logger)
      .debug(eq('Added qrc-file-generator task: {}'), eq('qrcFileGeneratorTask'))
  }

  @Test
  void shouldNotAddTaskWhenExtensionIsNull() {
    final boolean result = qrcFileGenerator.addTask('qrcFileGeneratorTask', null)

    assertFalse(result)
  }

  @Test
  void shouldNotAddTaskWhenNameIsNotAvailable() {
    spyProject.extensions.add(FileListerPlugin.EXTENSION_NAME, mock(FileListerExtension))
    spyProject.tasks.create('qrcFileGeneratorTask')

    final boolean result = qrcFileGenerator.addTask('qrcFileGeneratorTask', new QrcFileGeneratorConfig() {})

    assertFalse(result)
  }

  @Test
  void shouldNotAddTaskWhenFileListerNotFound() {
    final boolean result = qrcFileGenerator.addTask('qrcFileGeneratorTask', new QrcFileGeneratorConfig() {})

    assertFalse(result)
  }

  @Test
  void shouldAddRequiredPlugin() {
    qrcFileGenerator.addRequiredPlugin()

    verify(spyProject.logger)
      .debug(eq('qrc-file-generator extension applied {} plugin'), eq(FileListerPlugin))
  }

  @Test
  void shouldNotAddRequiredPluginWhenAlreadyAdded() {
    spyProject.extensions.add(FileListerPlugin.EXTENSION_NAME, mock(FileListerExtension))

    qrcFileGenerator.addRequiredPlugin()

    verify(spyProject.logger, never())
      .debug(eq('qrc-file-generator extension applied {} plugin'), any(Class))
  }
}
