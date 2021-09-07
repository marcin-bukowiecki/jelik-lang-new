package org.jelik.compiler.passes

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.exceptions.SyntaxException
import org.jelik.compiler.utils.Stateless
import org.jelik.parser.ast.ReturnExpr
import org.jelik.parser.ast.loops.ForEachLoop
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
@Stateless
object CodeBranchChecker : AstVisitor() {

    override fun visitReturnExpr(re: ReturnExpr, compilationContext: CompilationContext) {
        if (re.parent.parent is ForEachLoop) {
            throw SyntaxException("Unexpected return statement in loop body", re, compilationContext)
        }
        super.visitReturnExpr(re, compilationContext)
    }
}
