//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.qt.QrcFileGeneratorConfig

import groovy.transform.CompileStatic

import java.nio.file.Path

import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileVisitDetails

import org.junit.jupiter.api.Test

import org.mockito.invocation.InvocationOnMock

import static org.junit.jupiter.api.Assertions.assertEquals

import static org.mockito.Matchers.any
import static org.mockito.Mockito.doAnswer
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock

@CompileStatic
class QrcBodyGeneratorTest {
  private static final String qrcResultRelativized =
'''<!DOCTYPE RCC>
<RCC version\u003D"1.1">
  <qresource>
    <file>../source/path/someFile.ext</file>
  </qresource>
</RCC>
'''

  private static final String qrcResultNoRelativized =
'''<!DOCTYPE RCC>
<RCC version\u003D"1.1">
  <qresource>
    <file>someFile.ext</file>
  </qresource>
</RCC>
'''

  private static final String qrcResultWithRccPrefix =
'''<!DOCTYPE RCC>
<RCC version\u003D"1.1">
  <qresource prefix\u003D"/some">
    <file>../source/path/someFile.ext</file>
  </qresource>
</RCC>
'''

  private static final String qrcResultEmpty =
'''<!DOCTYPE RCC>
<RCC version\u003D"1.1">
  <qresource>
  </qresource>
</RCC>
'''

  @Test
  void shouldCalculateFilePath() {
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source/path'
    config.destinationFolder = 'dest'
    config.destinationFile = 'someQml.qrc'
    config.rccVersion = '1.1'
    final QrcBodyGenerator generator = new QrcBodyGenerator(config)

    final Path result = generator.calculateFilePath()

    assertEquals('dest/someQml.qrc', result.toString())
  }

  @Test
  void shouldCreateQrcBody() {
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source/path'
    config.destinationFolder = 'dest'
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
    final QrcBodyGenerator generator = new QrcBodyGenerator(config)

    final String result = generator.createQrcBody(mockConfigurableFileTree)

    assertEquals(qrcResultRelativized, result)
  }

  @Test
  void shouldCreateQrcBodyWhenNoRelativized() {
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source/path'
    config.destinationFolder = config.sourceFolder
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
    final QrcBodyGenerator generator = new QrcBodyGenerator(config)

    final String result = generator.createQrcBody(mockConfigurableFileTree)

    assertEquals(qrcResultNoRelativized, result)
  }

  @Test
  void shouldCreateQrcBodyWhenRccPrefixIsGiven() {
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source/path'
    config.destinationFolder = 'dest'
    config.rccVersion = '1.1'
    config.rccPrefix = '/some'
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
    final QrcBodyGenerator generator = new QrcBodyGenerator(config)

    final String result = generator.createQrcBody(mockConfigurableFileTree)

    assertEquals(qrcResultWithRccPrefix, result)
  }

  @Test
  void shouldCreateQrcBodyEmptyWhenNoFiles() {
    final QrcFileGeneratorConfig config = new QrcFileGeneratorConfig() {}
    config.sourceFolder = 'source/path'
    config.destinationFolder = 'dest'
    config.rccVersion = '1.1'
    final ConfigurableFileTree mockConfigurableFileTree = mock(ConfigurableFileTree)
    doAnswer { final InvocationOnMock invocation ->
      final FileVisitDetails mockFileInfo = mock(FileVisitDetails)
      doReturn(true)
        .when(mockFileInfo)
        .isDirectory()
      ((Closure)invocation.getArgument(0))
        .call(mockFileInfo)
      return null
    }
    .when(mockConfigurableFileTree)
    .visit(any(Closure))
    final QrcBodyGenerator generator = new QrcBodyGenerator(config)

    final String result = generator.createQrcBody(mockConfigurableFileTree)

    assertEquals(qrcResultEmpty, result)
  }
}
