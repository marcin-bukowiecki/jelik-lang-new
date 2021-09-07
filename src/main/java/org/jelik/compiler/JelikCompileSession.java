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

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author Marcin Bukowiecki
 */
public class JelikCompileSession {

    private final JelikCompileConfig config;

    private final JelikCompiler jelikCompiler;

    public JelikCompileSession(JelikCompileConfig config) {
        this.config = config;
        this.jelikCompiler = JelikCompiler.INSTANCE;
    }

    public JelikCompileConfig getConfig() {
        return config;
    }

    public File getLibPath() {
        try {
            return new File(Jlkc.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .getParentFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public JelikCompiler getJelikCompiler() {
        return jelikCompiler;
    }

    boolean isLogEnabled() {
        return config.isLogEnabled();
    }

    boolean isPrintByteCode() {
        return config.isPrintByteCode();
    }

    boolean createFiles() {
        return config.isCreateFiles();
    }

    String getCompileOutput() {
        return config.getOutputDirectory();
    }

    boolean isPrintCompilerException() {
        return config.isPrintCompilerException();
    }

    public static JelikCompileSession defaultSession() {
        return defaultSession(new JelikClassLoader());
    }

    public static JelikCompileSession defaultSession(JelikClassLoader classLoader) {
        return new JelikCompileSession(
                new JelikCompileConfig.Builder()
                        .setPrintCompilerException(true)
                        .setInputDirectory("./sandbox/in")
                        .setOutputDirectory("./sandbox/out")
                        .setCreateFiles(true)
                        .setLogEnabled(true)
                        .setRun(true)
                        .setLoadClasses(true)
                        .setJelikClassLoader(classLoader)
                        .build()
        );
    }

    String getInputDirectory() {
        return config.getInputDirectory();
    }

    public JelikClassLoader getJelikClassLoader() {
        return config.getJelikClassLoader();
    }

    boolean shouldRun() {
        return config.isRun();
    }

    public String getClassPath() {
        return "";
    }

    public boolean isLoadClasses() {
        return config.isLoadClasses();
    }

    public boolean isReplMode() {
        return false;
    }
}
