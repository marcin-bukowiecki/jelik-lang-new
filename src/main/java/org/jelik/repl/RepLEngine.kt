package org.jelik.repl

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.JelikCompiler
import org.jelik.compiler.utils.CompilationWrapper
import org.jelik.parser.ast.JelikASTFactory
import org.jelik.parser.ast.classes.ModuleDeclaration

/**
 * @author Marcin Bukowiecki
 */
class RepLEngine(private val ctx: CompilationContext = CompilationContext()) {

    fun eval(line: String?) {
        val node = JelikASTFactory.createExpressionFromText(ctx, line)
        if (node is ModuleDeclaration) {
            val compilationResult = CompilationWrapper.wrap{ JelikCompiler.INSTANCE.compileModules(listOf(node), ctx) }

        }
    }
}
