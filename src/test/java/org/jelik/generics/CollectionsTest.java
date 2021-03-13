package org.jelik.generics;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class CollectionsTest {

    @Test
    public void shouldCreateList_1() {
        var content = "" +
                "import java.util.List\n" +
                "\n" +
                "fun expr(a Int, b Int) -> List<Int> {\n" +
                "   ret List.of(a, b)\n" +
                "}";

        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("expr",11, 22)
                .isEqualTo(List.of(11, 22));
    }

    @Test
    public void shouldCreateList_2() {
        var content = "" +
                "import java.util.List\n" +
                "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(a Int, b Int) -> List<Int> {\n" +
                "   ret ArrayList<Int>()\n" +
                "}";

        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("expr",11, 22)
                .isEqualTo(List.of());
    }
}
