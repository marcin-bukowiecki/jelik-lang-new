package org.jelik.compiler.config

/**
 * @author Marcin Bukowiecki
 */
data class Config(private var mode: CompilerMode) {

    fun isReplMode(): Boolean {
        return mode == CompilerMode.repl
    }

    companion object {

        fun defaultConfig(): Config {
            return Config(mode = CompilerMode.standalone)
        }
    }
}
