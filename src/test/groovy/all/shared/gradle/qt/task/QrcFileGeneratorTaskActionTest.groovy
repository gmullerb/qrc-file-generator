//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.file.FileListerExtension
import all.shared.gradle.qt.QrcFileGeneratorConfig
import all.shared.gradle.testfixtures.SpyProjectFactory

import groovy.transform.CompileStatic

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileVisitDetails

import org.junit.jupiter.api.Test

import org.mockito.invocation.InvocationOnMock

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.doAnswer
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

@CompileStatic
class QrcFileGeneratorTaskActionTest {
  private static final String qrcResult =
'''<!DOCTYPE RCC>
<RCC version\u003D"1.1">
  <qresource>
    <file>source/path/someFile.ext</file>
  </qresource>
</RCC>
'''
  private final Project spyProject = SpyProjectFactory.build()

  @Test
  void shouldCreateNewFileWhenExecuting() {
    final FileListerExtension mockFileLister = mock(FileListerExtension)
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source/path'
    config.sourceExtensions = ['ext1', 'ext2']
    config.destinationFolder = ''
    config.destinationFile = 'someQml.qrc'
    config.rccVersion = '1.1'
    final ConfigurableFileTree mockConfigurableFileTree = mock(ConfigurableFileTree)
    doAnswer { final InvocationOnMock invocation ->
      final FileVisitDetails mockFileInfo = mock(FileVisitDetails)
      doReturn(false)
        .when(mockFileInfo)
        .isDirectory()
      doReturn('someFile.ext')
        .when(mockFileInfo)
        .getPath()
      ((Closure)invocation.getArgument(0))
        .call(mockFileInfo)
      return null
    }
    .when(mockConfigurableFileTree)
    .visit(any(Closure))
    doReturn(mockConfigurableFileTree)
      .when(mockFileLister)
      .obtainPartialFileTree(eq(config.sourceFolder), any(Map))
    final QrcFileGeneratorTaskAction generatorAction = new QrcFileGeneratorTaskAction(config, mockFileLister)
    final Task testTask = spyProject.tasks.create('testTask', QrcFileGeneratorTask)

    generatorAction.execute(testTask)
    generatorAction.execute(testTask)
    generatorAction.execute(testTask)

    assertEquals(qrcResult, spyProject.file('someQml.qrc').text)
  }

  @Test
  void shouldNotGenerateIfAnyError() {
    final FileListerExtension mockFileLister = mock(FileListerExtension)
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source?/path'
    config.sourceExtensions = ['ext1', 'ext2']
    config.destinationFolder = 'some?'
    config.destinationFile = 'some?Qml.qrc'
    config.rccVersion = '1.1'
    final ConfigurableFileTree mockConfigurableFileTree = mock(ConfigurableFileTree)
    doAnswer { final InvocationOnMock invocation ->
      final FileVisitDetails mockFileInfo = mock(FileVisitDetails)
      doReturn(false)
        .when(mockFileInfo)
        .isDirectory()
      doReturn('someFile.ext')
        .when(mockFileInfo)
        .getPath()
      ((Closure)invocation.getArgument(0))
        .call(mockFileInfo)
      return null
    }
    .when(mockConfigurableFileTree)
    .visit(any(Closure))
    doReturn(mockConfigurableFileTree)
      .when(mockFileLister)
      .obtainPartialFileTree(eq(config.sourceFolder), any(Map))
    final QrcFileGeneratorTaskAction generatorAction = new QrcFileGeneratorTaskAction(config, mockFileLister)

    generatorAction.generate(spyProject, config)

    verify(spyProject.logger)
      .error(eq('qrc-file-generator could not create qrc file, check task or extension configuration: {}'), eq(config))
  }

  @Test
  void shouldObtainResourceFiles() {
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source/path'
    config.sourceExtensions = ['ext1', 'ext2']
    config.excludes = ['exclude1', 'exclude2']
    final QrcFileGeneratorTaskAction generatorAction = new QrcFileGeneratorTaskAction(config, new FileListerExtension(spyProject))

    final ConfigurableFileTree result = generatorAction.obtainResourceFiles(config)

    assertEquals(['**/*.ext1', '**/*.ext2'] as Set, result.includes)
    assertTrue(result.excludes.contains('exclude1'))
    assertTrue(result.excludes.contains('exclude2'))
  }
}
