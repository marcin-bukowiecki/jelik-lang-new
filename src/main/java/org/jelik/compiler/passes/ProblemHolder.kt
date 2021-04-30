package org.jelik.compiler.passes

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.exceptions.CompileException
import org.jelik.compiler.utils.MessagesBundle
import org.jelik.parser.ast.ASTNode
import org.jelik.parser.ast.utils.ASTUtils
import org.jelik.types.Type
import org.jetbrains.annotations.Nls

/**
 * @author Marcin Bukowiecki
 */
class ProblemHolder(private val compilationContext: CompilationContext) {

    val problems = mutableListOf<ProblemDescriptor>()
    get() = field

    fun reportProblem(@Nls key: String, node: ASTNode) {
        problems.add(ProblemDescriptor(node, key, emptyArray()))
    }

    fun reportProblem(@Nls key: String, node: ASTNode, vararg types: Type) {
        problems.add(ProblemDescriptor(node, key, types))
    }

    fun hasProblems(): Boolean {
        return problems.isNotEmpty()
    }

    fun markProblems() {
        problems.forEach { problem ->
            val node = problem.node
            val moduleDeclaration = ASTUtils.getModuleDeclaration(node)
                ?: throw IllegalArgumentException("node without module declaration: $node")

            val message = if (problem.types.isEmpty()) {
                MessagesBundle.message(problem.messageKey)
            } else {
                MessagesBundle.message(problem.messageKey, *problem.types)
            }

            CompileException(message, node, moduleDeclaration).printErrorMessage()
        }
    }

    fun findProblemByCanonicalName(classCanonicalName: String): ProblemDescriptor? {
        return problems.firstOrNull { problem -> problem.getClassDeclaration()?.canonicalName == classCanonicalName }
    }
}
