package org.jelik.types;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author Marcin Bukowiecki
 */
public class TypeTest {

    @Test
    public void objectType() {
        Type type = new Type(Object.class);
        Assertions.assertThat(type)
                .hasFieldOrPropertyWithValue("name", "Object")
                .hasFieldOrPropertyWithValue("canonicalName", "java.lang.Object");
    }

    @Test
    public void stringType() {
        Type type = new Type(String.class);
        Assertions.assertThat(type)
                .hasFieldOrPropertyWithValue("name", "String")
                .hasFieldOrPropertyWithValue("canonicalName", "java.lang.String");
    }


    @Test
    public void serializableType() {
        Type type = new Type(Serializable.class);
        Assertions.assertThat(type)
                .hasFieldOrPropertyWithValue("name", "Serializable")
                .hasFieldOrPropertyWithValue("canonicalName", "java.io.Serializable");
    }

    @Test
    public void intType() {
        Type type = new Type(int.class);
        Assertions.assertThat(type)
                .hasFieldOrPropertyWithValue("name", "int")
                .hasFieldOrPropertyWithValue("canonicalName", "int");
    }
}
