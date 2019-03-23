//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt

import all.shared.gradle.testfixtures.SpyProjectFactory

import groovy.transform.CompileStatic

import org.gradle.api.Project

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
class QrcFileGeneratorPluginTest {
  private final Project spyProject = SpyProjectFactory.build()

  @Test
  void shouldApplyPlugin() {
    final QrcFileGeneratorPlugin qrcFileGenerator = new QrcFileGeneratorPlugin()

    qrcFileGenerator.apply(spyProject)

    assertTrue(spyProject.properties[QrcFileGeneratorPlugin.EXTENSION_NAME] instanceof QrcFileGeneratorConfig)
    assertNotNull(spyProject.tasks.findByPath(QrcFileGeneratorPlugin.TASK_NAME))
  }
}
