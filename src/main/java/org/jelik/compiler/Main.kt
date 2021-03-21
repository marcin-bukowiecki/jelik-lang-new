package org.jelik.compiler

/**
 * Entry point for Jelik compiler
 *
 * @author Marcin Bukowiecki
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        JelikCompiler.INSTANCE.compile(args)
    }
}
