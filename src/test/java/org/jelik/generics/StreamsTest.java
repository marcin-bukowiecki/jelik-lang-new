package org.jelik.generics;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.stream.Stream;

/**
 * @author Marcin Bukowiecki
 */
public class StreamsTest {

    @Test
    public void shouldCompileGivenStream() {
        var content = "" +
                "import java.util.stream.Stream\n" +
                "import java.util.stream.Collectors\n" +
                "\n" +
                "fun ints(stream Stream<Int>) -> Int {\n" +
                "   ret stream.collect(Collectors.toList()).get(0)\n" +
                "}";

        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("ints", Stream.of(1,3,5,7,9))
                .isEqualTo(1);
    }
}
