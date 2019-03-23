//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt.task

import all.shared.gradle.file.FileListerExtension
import all.shared.gradle.qt.QrcFileGeneratorConfig
import all.shared.gradle.testfixtures.SpyProjectFactory

import groovy.transform.CompileStatic

import org.gradle.api.Project
import org.gradle.api.Task

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

import static org.mockito.Mockito.mock

@CompileStatic
class QrcFileGeneratorTaskConfigurationActionTest {
  private final Project spyProject = SpyProjectFactory.build()

  @Test
  void shouldConfigureTask() {
    final QrcFileGeneratorConfig mockConfig = mock(QrcFileGeneratorConfig)
    final FileListerExtension mockFileLister = mock(FileListerExtension)
    final QrcFileGeneratorTaskConfigurationAction action = new QrcFileGeneratorTaskConfigurationAction(mockConfig, mockFileLister)
    final Task task = spyProject.tasks.create('testTask')

    action.execute(task)

    assertEquals(1, task.actions.size())
  }
}
