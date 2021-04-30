package org.jelik.repl

import org.jelik.compiler.config.CompilationContext

/**
 * @author Marcin Bukowiecki
 */
object JelikRepl {

    @JvmStatic
    fun main(args: Array<String>) {
        val engine = RepLEngine(CompilationContext())
        println("Jelik 1.0.0")
        while (true) {
            print(">>>")
            val line = readLine()
            engine.eval(line)
        }
    }
}
