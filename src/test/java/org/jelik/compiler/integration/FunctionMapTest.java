package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.*;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionMapTest {

    @Test
    public void createMap_1() {
        var expr = "import java.util.Map\nfun expr() -> Map<String, Int> {" +
                "   ret mapOf{\"foo\": 11, \"bar\": 12}" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(Map.of("foo", 11, "bar", 12));
    }

    @Test
    public void createMap_2() {
        var expr = "import java.util.Map\nfun expr() -> Int {" +
                "   ret mapOf{\"foo\": 11, \"bar\": 12}[\"foo\"]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(11);
    }

    @Test
    public void createMap_3() {
        var expr = "import java.util.Map\nfun expr() -> Int {" +
                "   ret mapOf{\"foo\": 11, \"bar\": 12}[\"foo\"] - 1" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(10);
    }

    @Test
    public void createMap_4() {
        var expr = "import java.util.Map\nfun expr() -> Int {" +
                "   ret mapOf{\"foo\": 11, \"bar\": 12}.size()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(2);
    }

    @Test
    public void createMap_5() {
        var expr = "import java.util.Set\nfun expr() -> Set<String> {" +
                "   ret mapOf{\"foo\": 11, \"bar\": 12}.keySet()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(Set.of("foo", "bar"));
    }

    @Test
    public void createMap_6() {
        var expr = "import java.util.Collection\nfun expr() -> Collection<Int> {" +
                "   ret mapOf{\"foo\": 11, \"bar\": 12}.values()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .mapResult(HashSet::new)
                .isEqualTo(Set.of(11, 12));
    }

    @Test
    public void createEmptyMap_1() {
        var expr = "import java.util.Collection\nfun expr() -> Collection<Int> {" +
                "   ret mapOf{}.values()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .mapResult(HashSet::new)
                .isEqualTo(Set.of());
    }

    @Test
    public void assignValueToMap_1() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Map<String, Int> {\n" +
                "   val map = mapOf{\"test1\": 123}\n" +
                "   map[\"test\"] = 100\n" +
                "   ret map" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Map<String, Integer>>invoke("expr")
                .isEqualTo(Map.of("test1", 123, "test", 100));
    }

    @Test
    public void assignValueToMap_2() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Map<String, Int> {\n" +
                "   val map = mapOf{1: 123}\n" +
                "   map[2] = 100\n" +
                "   ret map" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Map<Integer, Integer>>invoke("expr")
                .isEqualTo(Map.of(1, 123, 2, 100));
    }

    @Test
    public void assignValueToMap_3() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Collection<Int> {\n" +
                "   val map = mapOf{1: 123}\n" +
                "   map[2] = 100\n" +
                "   ret map.values()" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .mapResult(ArrayList::new)
                .isEqualTo(List.of(123, 100));
    }

    @Test
    public void emptyMap_1() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Map<Int, Int> {\n" +
                "   val map Map<Int, Int> = mapOf{}\n" +
                "   map[2] = 100\n" +
                "   ret map" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .isEqualTo(Map.of(2, 100));
    }

    @Test
    public void emptyMap_2() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Map<Int, Int> {\n" +
                "   val map Map<Int, Int> = mapOf{}\n" +
                "   ret map" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .isEqualTo(Map.of());
    }

    @Test
    public void emptyMap_3() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Map<Int, Int> {\n" +
                "   val map Map<Int, Int> = mapOf{}\n" +
                "   map[2] = 100\n" +
                "   map[3] = 100\n" +
                "   ret map" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .isEqualTo(Map.of(2, 100, 3, 100));
    }

    @Test
    public void emptyMap_4() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Map<Int, Any> {\n" +
                "   val map Map<Int, Any> = mapOf{}\n" +
                "   map[2] = 100\n" +
                "   map[3] = \"100\"\n" +
                "   ret map" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Collection<Integer>>invoke("expr")
                .isEqualTo(Map.of(2, 100, 3, "100"));
    }

    @Test
    public void sizeOfMap_1() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Int {\n" +
                "   val map Map<Int, Any> = mapOf{}\n" +
                "   map[2] = 100\n" +
                "   map[3] = \"100\"\n" +
                "   ret len(map)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Integer>invoke("expr")
                .isEqualTo(2);
    }

    @Test
    public void sizeOfMap_2() {
        var expr = "import java.util.Collection\n" +
                "import java.util.Map\n" +
                "fun expr() -> Int {\n" +
                "   val map Map<Int, Any> = mapOf{}\n" +
                "   ret len(map)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .<Integer>invoke("expr")
                .isEqualTo(0);
    }
}
