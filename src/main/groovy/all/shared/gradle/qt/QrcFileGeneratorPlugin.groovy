//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt

import groovy.transform.CompileStatic

import org.gradle.api.Plugin
import org.gradle.api.Project

@CompileStatic
class QrcFileGeneratorPlugin implements Plugin<Project> {
  public static final String EXTENSION_NAME = 'qrcFileGenerator'
  public static final String TASK_NAME = 'generateQrcFile'

  protected static final boolean complement(final QrcFileGenerator qrcFileGenerator) {
    qrcFileGenerator.complement(EXTENSION_NAME, TASK_NAME)
  }

  public void apply(final Project project) {
    complement(new QrcFileGenerator(project))
  }
}
