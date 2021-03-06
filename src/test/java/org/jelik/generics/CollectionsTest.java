package org.jelik.generics;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void shouldCreateList_3() {
        var content = "" +
                "import java.util.List\n" +
                "\n" +
                "fun expr(a Int, b Int) -> List<Int> {\n" +
                "   ret List.of()\n" +
                "}";

        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("expr",11, 22)
                .isEqualTo(List.of());
    }

    @Test
    public void shouldCreateList_4() {
        var content = "" +
                "import java.util.List\n" +
                "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(a Int, b Int) -> List<Int> {\n" +
                "   ret List.of(b)\n" +
                "}";

        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("expr",11, 22)
                .isEqualTo(List.of(22));
    }

    @Test
    public void getElementFromArrayList_1() {
        var content = "" +
                "import java.util.List\n" +
                "\n" +
                "fun expr(a List<Int>) -> Int {\n" +
                "   ret a.get(1)\n" +
                "}\n";
        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("expr",List.of(12,34,56))
                .isEqualTo(34);
    }

    @Test
    public void getArrayList_1() {
        var content = "" +
                "import java.util.List\n" +
                "import java.util.ArrayList\n" +
                "\n" +
                "fun expr() -> List<Int> {\n" +
                "   ret ArrayList<Int>()\n" +
                "}\n";
        FunctionCompiler.getInstance()
                .compile(content)
                .invoke("expr")
                .isEqualTo(new ArrayList<Integer>());
    }
}
