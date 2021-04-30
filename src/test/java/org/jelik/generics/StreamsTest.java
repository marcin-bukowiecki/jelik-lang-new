package org.jelik.generics;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marcin Bukowiecki
 */
public class StreamsTest {

    @Test
    public void shouldCompileGivenStream_1() {
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

    @Test
    public void shouldCompileGivenStream_2() {
        var content = "" +
                "import java.util.stream.Stream\n" +
                "import java.util.List\n" +
                "import java.util.stream.Collectors\n" +
                "\n" +
                "fun ints(stream Stream<Int>) -> List<Int> {\n" +
                "   ret stream.collect(Collectors.toList())\n" +
                "}";

        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("ints", Stream.of(1,3,5,7,9))
                .isEqualTo(Arrays.asList(1,3,5,7,9));
    }

    @Test
    public void shouldCompileGivenStream_3() {
        var content = "" +
                "import java.util.ArrayList\n" +
                "import java.util.List\n" +
                "import java.util.stream.Collectors\n" +
                "\n" +
                "fun ints(l ArrayList<Int>) -> List<Int> {\n" +
                "   ret l.stream().collect(Collectors.toList())\n" +
                "}";

        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("ints", new ArrayList<>(Stream.of(1, 3, 5, 7, 9).collect(Collectors.toList())))
                .isEqualTo(Arrays.asList(1,3,5,7,9));
    }
}
