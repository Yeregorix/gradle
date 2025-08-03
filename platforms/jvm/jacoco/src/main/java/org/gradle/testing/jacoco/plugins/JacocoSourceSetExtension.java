/*
 * Copyright 2025 the original author or authors.
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

package org.gradle.testing.jacoco.plugins;

import org.gradle.api.Incubating;
import org.gradle.api.file.FileCollection;

/**
 * Extension containing Jacoco instrumented classes for source sets.
 *
 * @since 8.14.3
 */
@Incubating
public class JacocoSourceSetExtension {

    private final FileCollection instrumentedClasses;

    /**
     * Creates an extension containing Jacoco instrumented classes.
     *
     * @since 8.14.3
     */
    public JacocoSourceSetExtension(FileCollection instrumentedClasses) {
        this.instrumentedClasses = instrumentedClasses;
    }

    /**
     * The collection of offline instrumented classes generated from this source set.
     *
     * @since 8.14.3
     */
    public FileCollection getInstrumentedClasses() {
        return instrumentedClasses;
    }
}
