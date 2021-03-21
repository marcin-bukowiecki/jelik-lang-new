package org.jelik.compiler.integration;

import org.assertj.core.api.Assertions;
import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionTryCatchTest {

    @Test
    public void returnBooleanFromSimpleEqualExpr_1() {
        var expr = "fun expr(a Int) -> Int {\n" +
                "   try {\n" +
                "       a = a / 0 \n" +
                  "   } catch (e Throwable) { \n" +
                "       e.printStackTrace() \n" +
                "       ret 111\n" +
                "   }\n" +
                "   ret 123\n" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(111);
    }

    @Test
    public void returnBooleanFromSimpleEqualExpr_2() {
        var expr = "fun expr(a Int) -> Int {\n" +
                "   try {\n" +
                "       a = a / 0 \n" +
                "   } catch (e Throwable) { \n" +
                "       ret 111\n" +
                "   }\n" +
                "   ret 123\n" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(111);
    }

    @Test
    public void returnBooleanFromSimpleEqualExpr_3() {
        var expr = "fun expr(a Int) -> Int {\n" +
                "   try {\n" +
                "       a = a / 0 \n" +
                "   } catch (e Throwable) { \n" +
                "   }\n" +
                "   ret 123\n" +
                "}";

        Assertions.assertThatThrownBy(() -> FunctionCompiler.getInstance()
                .compile(expr))
                .hasMessage("Empty catch block");
    }
}
