package org.jelik.compiler.utils;

import org.assertj.core.api.Assertions;
import org.jelik.compiler.exceptions.CompileException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class CompilationResult {

    private final Class<?> compiledClass;

    public CompilationResult(Class<?> compiledClass) {
        this.compiledClass = compiledClass;
    }

    public StaticInvocationResult invoke(String methodName, Object... args) {
        List<Method> collect = Arrays.stream(compiledClass.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            throw new IllegalArgumentException("Could not find method: " + methodName);
        }

        Method method = collect.get(0);

        try {
            return new StaticInvocationResult(method.invoke(this, args));
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public void invokeAndExpectError(String expr, String message, Object... args) {
        try {
            invoke(expr, args);
        } catch (CompileException ex) {
            ex.printErrorMessage();
            Assertions.assertThat(ex.getMessage()).isEqualTo(message);
        }
    }
}
