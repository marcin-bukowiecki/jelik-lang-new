package org.jelik.compiler.utils;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;

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

    @SuppressWarnings("unchecked")
    public <T> StaticInvocationResult<T> invoke(String methodName, Object... args) {
        List<Method> collect = Arrays.stream(compiledClass.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            throw new IllegalArgumentException("Could not find method: " + methodName);
        }

        Method method = collect.get(0);

        try {
            return new StaticInvocationResult<T>(((T) method.invoke(this, args)));
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isInterface() {
        return compiledClass.isInterface();
    }

    public void invokeAndExpectError(String expr, Class<?> exceptionType, Object... args) {
        try {
            invoke(expr, args);
        } catch (Throwable ex) {
            Assertions.assertThat(ex.getClass()).isEqualTo(exceptionType);
        }
    }

    public void invokeAndExpectError(String expr, String message, Object... args) {
        try {
            invoke(expr, args);
        } catch (Throwable ex) {
            Assertions.assertThat(ex.getMessage()).isEqualTo(message);
        }
    }

    public boolean hasMethod(@NotNull String name) {
        return Arrays.stream(compiledClass.getDeclaredMethods()).anyMatch(m -> m.getName().equals(name));
    }
}
