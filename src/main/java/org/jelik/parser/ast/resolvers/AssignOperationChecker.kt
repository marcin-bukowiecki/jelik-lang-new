package org.jelik.parser.ast.resolvers

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
class AssignOperationChecker : AstVisitor() {

    override fun visitArrayOrMapGetExpr(expr: ArrayOrMapGetExpr, compilationContext: CompilationContext) {

    }
}
