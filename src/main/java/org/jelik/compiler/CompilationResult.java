package org.jelik.compiler;

import java.lang.reflect.Method;

public class CompilationResult {

    private final Class<?> clazz;

    public CompilationResult(Class<?> clazz) {
        this.clazz = clazz;
    }

    public MethodWrapper method(String methodName,
                         Class<?>... argTypes) {
        try {
            return new MethodWrapper(clazz.getMethod(methodName, argTypes));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static class MethodWrapper {

        private final Method method;

        public MethodWrapper(Method method) {
            this.method = method;
        }

        public Object invokeStatic(Object... args) {
            try {
                return method.invoke(null, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
