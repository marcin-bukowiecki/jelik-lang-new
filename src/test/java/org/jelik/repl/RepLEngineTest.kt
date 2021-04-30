package org.jelik.repl

import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class RepLEngineTest {

    @Test
    fun testRepl_1() {
        val expr = "2+2"
        val result = RepLEngine().eval(expr)

    }
}
