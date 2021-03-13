package org.jelik.compiler.data;

import org.assertj.core.api.Assertions;
import org.jelik.parser.ast.types.JelikGenericType;
import org.jelik.parser.ast.types.JelikWildCardType;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @author Marcin Bukowiecki
 */
public class MethodDataTest {

    @Test
    public void shouldGetWildCardType() throws NoSuchMethodException {
        Method method = Stream.class.getMethod("collect", Collector.class);
        MethodDataImpl methodData = new MethodDataImpl(method, new ClassDataImpl(Collector.class));
        Assertions.assertThat(methodData.getGenericParameterTypes().get(0).getTypeVariables().get(0))
                .isInstanceOf(JelikWildCardType.class);
        Assertions.assertThat(methodData.getGenericParameterTypes().get(0).getTypeParameters().get(0))
                .isInstanceOf(JelikGenericType.class);
    }
}
