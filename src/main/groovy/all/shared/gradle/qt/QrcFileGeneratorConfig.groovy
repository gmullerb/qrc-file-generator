//  Copyright (c) 2019 Gonzalo Müller Bravo.
//  Licensed under the MIT License (MIT), see LICENSE.txt
package all.shared.gradle.qt

import groovy.transform.CompileStatic

@CompileStatic
trait QrcFileGeneratorConfig {
  String sourceFolder
  List<String> sourceExtensions
  String destinationFile
  String destinationFolder
  List<String> excludes
  String rccVersion
  String rccPrefix
}
