//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt

import groovy.transform.CompileStatic

import org.junit.jupiter.api.Test

import pl.pojo.tester.api.assertion.Method

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor

@CompileStatic
class QrcFileGeneratorConfigTest {
  @Test
  void shouldWorkAsPogo() {
    assertPojoMethodsFor(QrcFileGeneratorConfig)
      .testing(Method.SETTER, Method.GETTER)
      .quickly()
      .areWellImplemented()
  }
}
