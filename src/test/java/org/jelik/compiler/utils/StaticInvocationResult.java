package org.jelik.compiler.utils;

import org.assertj.core.api.Assertions;

/**
 * @author Marcin Bukowiecki
 */
public class StaticInvocationResult {

    private final Object value;

    public StaticInvocationResult(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void isEqualTo(Object given) {
        Assertions.assertThat(this.value)
                .isEqualTo(given);
    }
}
