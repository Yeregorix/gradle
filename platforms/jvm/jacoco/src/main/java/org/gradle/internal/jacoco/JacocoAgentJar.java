/*
 * Copyright 2013 the original author or authors.
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
package org.gradle.internal.jacoco;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.file.FileOperations;
import org.gradle.util.internal.VersionNumber;

import java.io.File;
import javax.inject.Inject;

/**
 * Helper to resolve the runtime agent jar.
 */
public class JacocoAgentJar {

    private static final VersionNumber V_0_6_2_0 = VersionNumber.parse("0.6.2.0");
    private static final VersionNumber V_0_7_6_0 = VersionNumber.parse("0.7.6.0");

    private final FileOperations fileOperations;
    private FileCollection agentConf;
    private File runtimeJar;

    /**
     * Constructs a new agent JAR wrapper.
     */
    @Inject
    public JacocoAgentJar(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    /**
     * @return the configuration that the agent JAR is located in
     */
    public FileCollection getAgentConf() {
        return agentConf;
    }

    public void setAgentConf(FileCollection agentConf) {
        this.agentConf = agentConf;
    }

    /**
     * @return a file pointing to the runtime agent jar.
     */
    public File getJar() {
        if (runtimeJar == null) {
            File candidate = getAgentConf().getSingleFile();
            if (candidate.getName().endsWith("-runtime.jar")) {
                runtimeJar = candidate;
            } else {
                // Legacy, unzips the resolved jar to retrieve the runtime jar.
                runtimeJar = fileOperations.zipTree(candidate).filter(file -> file.getName().equals("jacocoagent.jar")).getSingleFile();
            }
        }
        return runtimeJar;
    }

    public boolean supportsJmx() {
        return V_0_6_2_0.compareTo(extractVersion()) <= 0;
    }

    public boolean supportsInclNoLocationClasses() {
        return V_0_7_6_0.compareTo(extractVersion()) <= 0;
    }

    private VersionNumber extractVersion() {
        // format: org.jacoco.agent-<version>(-runtime).jar
        String jarName = getAgentConf().getSingleFile().getName();
        int versionStart = "org.jacoco.agent-".length();
        int versionEnd = jarName.length() - (jarName.endsWith("-runtime.jar") ? 12 : 4);
        return VersionNumber.parse(jarName.substring(versionStart, versionEnd));
    }
}
