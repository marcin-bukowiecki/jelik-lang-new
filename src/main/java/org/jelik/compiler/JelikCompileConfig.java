/*
 * Copyright 2019 Marcin Bukowiecki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jelik.compiler;

import org.jelik.compiler.cl.JelikClassLoader;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public final class JelikCompileConfig {

    private final boolean printCompilerException;

    private final String outputDirectory;

    private final String inputDirectory;

    private final boolean printByteCode;

    private final boolean logEnabled;

    private final boolean run;

    private final boolean createFiles;

    private final boolean loadClasses;

    private final JelikClassLoader jelikClassLoader;

    private final List<String> filesToCompile;

    public JelikCompileConfig(List<String> filesToCompile,
                              boolean printCompilerException,
                              String outputDirectory,
                              String inputDirectory,
                              boolean printByteCode,
                              boolean logEnabled,
                              boolean run,
                              boolean createFiles,
                              boolean loadClasses,
                              JelikClassLoader jelikClassLoader) {

        this.filesToCompile = filesToCompile;
        this.printCompilerException = printCompilerException;
        this.outputDirectory = outputDirectory;
        this.inputDirectory = inputDirectory;
        this.printByteCode = printByteCode;
        this.logEnabled = logEnabled;
        this.run = run;
        this.createFiles = createFiles;
        this.loadClasses = loadClasses;
        this.jelikClassLoader = jelikClassLoader;
    }

    public List<String> getFilesToCompile() {
        return filesToCompile;
    }

    public JelikClassLoader getJelikClassLoader() {
        return jelikClassLoader;
    }

    public boolean isPrintCompilerException() {
        return printCompilerException;
    }

    public boolean isCreateFiles() {
        return createFiles;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public boolean isPrintByteCode() {
        return printByteCode;
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public boolean isRun() {
        return run;
    }

    public boolean isLoadClasses() {
        return loadClasses;
    }

    public static class Builder {

        private List<String> filesToCompile = Collections.emptyList();

        private boolean printCompilerException = true;

        private String outputDirectory = "./sandbox/out";

        private String inputDirectory = "";

        private JelikClassLoader jelikClassLoader;

        private boolean printByteCode = true;

        private boolean logEnabled = true;

        private boolean run = true;

        private boolean createFiles = true;

        private boolean loadClasses = false;

        public Builder setFilesToCompile(List<String> filesToCompile) {
            this.filesToCompile = filesToCompile;
            return this;
        }

        public Builder setJelikClassLoader(JelikClassLoader jelikClassLoader) {
            this.jelikClassLoader = jelikClassLoader;
            return this;
        }

        public Builder setPrintCompilerException(boolean printCompilerException) {
            this.printCompilerException = printCompilerException;
            return this;
        }

        public Builder setOutputDirectory(String outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public Builder setInputDirectory(String inputDirectory) {
            this.inputDirectory = inputDirectory;
            return this;
        }

        public Builder setPrintByteCode(boolean printByteCode) {
            this.printByteCode = printByteCode;
            return this;
        }

        public Builder setLogEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        public Builder setRun(boolean run) {
            this.run = run;
            return this;
        }

        public Builder setCreateFiles(boolean createFiles) {
            this.createFiles = createFiles;
            return this;
        }

        public Builder setLoadClasses(boolean loadClasses) {
            this.loadClasses = loadClasses;
            return this;
        }

        public JelikCompileConfig build() {
            return new JelikCompileConfig(
                    filesToCompile,
                    printCompilerException,
                    outputDirectory,
                    inputDirectory,
                    printByteCode,
                    logEnabled,
                    run,
                    createFiles,
                    loadClasses,
                    jelikClassLoader == null ? new JelikClassLoader() : jelikClassLoader
            );
        }
    }
}
