package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.jelik.utils.DependencyLoader;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionTypesTest {

    @Test
    public void checkForString_1() {
        var expr = "fun expr(a Object) -> Boolean { ret a is String }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo")
                .isEqualTo(true);
    }

    @Test
    public void checkForString_2() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk");
        var expr = "fun expr() -> Boolean { ret [\"foo\", \"bar\"][1] is String }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(true);
    }

    @Test
    public void checkForString_3() {
        var expr = "fun expr(a Object) -> Boolean { ret (a + \"bar\") is String }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo")
                .isEqualTo(true);
    }

    @Test
    public void checkForInt_1() {
        var expr = "fun expr(a Object) -> Boolean { ret a is Int }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo")
                .isEqualTo(false);
    }

    @Test
    public void checkForInt_2() {
        var expr = "fun expr(a Int) -> Boolean { ret a is Int }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 1)
                .isEqualTo(true);
    }

    @Test
    public void checkForInt_3() {
        var expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(a Int) -> Boolean { \n" +
                "   val b = ArrayList<Int>()\n" +
                "   b.add(123)\n" +
                "   ret b.get(0) is Int\n" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 1)
                .isEqualTo(true);
    }
}
