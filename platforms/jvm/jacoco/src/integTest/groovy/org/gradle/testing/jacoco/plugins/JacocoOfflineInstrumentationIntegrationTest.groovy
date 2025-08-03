/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.testing.jacoco.plugins

import org.gradle.integtests.fixtures.TargetCoverage
import org.gradle.testing.jacoco.plugins.fixtures.JacocoCoverage

@TargetCoverage({ JacocoCoverage.supportedVersionsByJdk })
class JacocoOfflineInstrumentationIntegrationTest extends JacocoMultiVersionIntegrationTest {

    def setup() {
        javaProjectUnderTest.writeSourceFiles()
    }

    def "task jacocoMainInstrumentedClasses is not executed by default"() {
        when:
        succeeds('test')

        then:
        notExecuted(':jacocoMainInstrumentedClasses')
    }

    def "instrumented classes are generated when offline instrumentation is enabled"() {
        given:
        javaProjectUnderTest.writeOfflineInstrumentation(true)

        when:
        succeeds('test')

        then:
        executedAndNotSkipped(':jacocoMainInstrumentedClasses')
        file("build/jacoco/test.exec").assertIsFile()
        file("build/jacoco/instrumented-classes/main").assertContainsDescendants("org/gradle/Class1.class")
    }

    def "offline instrumentation with multiple sourceSets"() {
        given:
        buildFile << """
            sourceSets {
                extra {
                }
            }

            dependencies {
                testImplementation sourceSets.extra.output
            }
        """
        javaProjectUnderTest.writeOfflineInstrumentation(true, "main", "extra")
        javaProjectUnderTest.writeSourceFiles(1, "extra", "Extra")

        when:
        succeeds('test')

        then:
        executedAndNotSkipped(':jacocoMainInstrumentedClasses', ':jacocoExtraInstrumentedClasses')
        file("build/jacoco/test.exec").assertIsFile()
        file("build/jacoco/instrumented-classes/main").assertContainsDescendants("org/gradle/Class1.class")
        file("build/jacoco/instrumented-classes/extra").assertContainsDescendants("org/gradle/Class1Extra.class")
    }
}
