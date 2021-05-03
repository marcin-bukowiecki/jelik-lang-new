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

package org.jelik.compiler.utils;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.jelik.compiler.JelikCompiler;
import org.jelik.compiler.asm.utils.ByteCodeLogger;
import org.jelik.compiler.asm.visitor.ToByteCodeResult;
import org.jelik.compiler.cl.JelikClassLoader;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.compiler.passes.ProblemDescriptor;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
@VisibleForTesting
public class FunctionCompiler {

    private static final Logger log = Logger.getLogger(FunctionCompiler.class.getCanonicalName());

    private static int nameCounter = 0;

    private static final FunctionCompiler INSTANCE = new FunctionCompiler();

    private final JelikClassLoader jelikClassLoader = new JelikClassLoader();

    private FunctionCompiler() {
    }

    public static FunctionCompiler getInstance() {
        return INSTANCE;
    }

    public JelikClassLoader getJelikClassLoader() {
        return jelikClassLoader;
    }

    public CompilationResult compile(final String expression) {
        return new CompilationResult(compile("JelikClass" + nameCounter++, expression));
    }

    public Class<?> compile(final String className, final String expression) {
        return compile(className, expression, new CompilationContext());
    }

    public @Nullable Class<?> compile(final String className,
                                      final String expression,
                                      final CompilationContext compilationContext) {

        log.info("Compiling class: " + className);
        var fileName =  className + ".jlk";
        var tempFile = new File("./src/test/resources/classes/" + fileName);
        try {
            FileUtils.writeByteArrayToFile(tempFile, expression.getBytes());
            List<ToByteCodeResult> result = JelikCompiler.INSTANCE
                    .compile(Collections.singletonList(tempFile), compilationContext);

            for (ToByteCodeResult toByteCodeResult : result) {
                ByteCodeLogger.logASM(toByteCodeResult.getBytes());
            }

            final List<? extends Class<?>> classes = result.stream().map(bcr -> jelikClassLoader.defineClass(
                    bcr.getType().getCanonicalName(),
                    bcr.getBytes())).collect(Collectors.toList());

            return classes.stream()
                    .filter(c -> c.getSimpleName().equals(className))
                    .findFirst()
                    .orElse(null);
        } catch (CompileException ex) {
            ex.printErrorMessage();
            throw ex;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object compileAndGetInstance(final String className, final String expression) throws Exception {
        return compile(className, expression).getConstructor().newInstance();
    }

    public Object compileAndGetInstance(final String expression) {
        try {
            return compile(getClassName(), expression).getConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getClassName() {
        return "JelikClass" + nameCounter++;
    }

    public void compileAndExpectError(String expr, String message) {
        try {
            compile("JelikClass" + nameCounter++, expr);
        } catch (CompileException ex) {
            Assertions.assertThat(ex.getMessage()).isEqualTo(message);
        }
    }

    public void compileAndExpectProblem(String expr, String message) {
        final CompilationContext compilationContext = new CompilationContext();
        final String className = "JelikClass" + nameCounter++;
        compile(className, expr, compilationContext);
        Assertions.assertThat(compilationContext
                .getProblemHolder()
                .getProblems().stream().map(ProblemDescriptor::getMessageText).collect(Collectors.toList()))
                .anyMatch(m -> m.equals(message));
    }
}
