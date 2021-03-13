package org.jelik.parser.ast.resolvers

import org.jelik.CompilationContext
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
class AssignOperationChecker : AstVisitor() {

    override fun visit(expr: ArrayOrMapGetExpr, compilationContext: CompilationContext) {

    }
}
