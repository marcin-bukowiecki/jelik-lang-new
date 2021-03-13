package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionTryCatchTest {

    @Test
    public void returnBooleanFromSimpleEqualExpr() {
        var expr = "fun expr(a Int) -> Int {\n" +
                "   try {\n" +
                "       a = a / 0 \n" +
             //   "       ret a / 0" +
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
}
