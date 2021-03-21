package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionMapTest {

    @Test
    public void createMap_1() {
        var expr = "import java.util.Map\nfun expr() -> Map<String, Int> {" +
                "   ret {\"foo\": 11, \"bar\": 12}" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(Map.of("foo", 11, "bar", 12));
    }

    @Test
    public void createMap_2() {
        var expr = "import java.util.Map\nfun expr() -> Int {" +
                "   ret {\"foo\": 11, \"bar\": 12}[\"foo\"]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(11);
    }

    @Test
    public void createMap_3() {
        var expr = "import java.util.Map\nfun expr() -> Int {" +
                "   ret {\"foo\": 11, \"bar\": 12}[\"foo\"] - 1" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(10);
    }

    @Test
    public void createMap_4() {
        var expr = "import java.util.Map\nfun expr() -> Int {" +
                "   ret {\"foo\": 11, \"bar\": 12}.size()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(2);
    }

    @Test
    public void createMap_5() {
        var expr = "import java.util.Set\nfun expr() -> Set<String> {" +
                "   ret {\"foo\": 11, \"bar\": 12}.keySet()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(Set.of("foo", "bar"));
    }

    @Test
    public void createMap_6() {
        var expr = "import java.util.Collection\nfun expr() -> Collection<Int> {" +
                "   ret {\"foo\": 11, \"bar\": 12}.values()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .mapResult(HashSet::new)
                .isEqualTo(Set.of(11, 12));
    }
}
