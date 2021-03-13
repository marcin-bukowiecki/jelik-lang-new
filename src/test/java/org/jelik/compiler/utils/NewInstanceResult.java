package org.jelik.compiler.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class NewInstanceResult {

    private final Object instance;

    public NewInstanceResult(Object instance) {
        this.instance = instance;
    }

    public InstanceInvocationResult invoke(String methodName,
                                           Object... args) {

        return null;
    }
}
