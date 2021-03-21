package org.jelik.compiler.utils;

import org.assertj.core.api.Assertions;

import java.util.function.Function;

/**
 * @author Marcin Bukowiecki
 */
public class StaticInvocationResult<T> {

    private final T value;

    public StaticInvocationResult(T value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void isEqualTo(Object given) {
        Assertions.assertThat(this.value)
                .isEqualTo(given);
    }

    public <R> StaticInvocationResult<R> mapResult(Function<T, R> mapper) {
        return new StaticInvocationResult<R>(mapper.apply(this.value));
    }
}
