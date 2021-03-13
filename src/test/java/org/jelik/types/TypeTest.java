package org.jelik.types;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TypeTest {

    @Test
    public void objectType() {
        Type type = new Type(Object.class);
        Assertions.assertThat(type)
                .hasFieldOrPropertyWithValue("name", "Object")
                .hasFieldOrPropertyWithValue("canonicalName", "java.lang.Object");
    }
}
