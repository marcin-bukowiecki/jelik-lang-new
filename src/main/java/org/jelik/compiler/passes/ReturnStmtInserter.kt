package org.jelik.compiler.passes

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ReturnExpr
import org.jelik.parser.ast.blocks.BasicBlock
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * Visitor to insert void return statements
 *
 * @author Marcin Bukowiecki
 */
object ReturnStmtInserter : AstVisitor() {

    override fun visitFunctionDeclaration(functionDeclaration: FunctionDeclaration,
                                          compilationContext: CompilationContext
    ) {
        compilationContext.pushCompilationUnit(functionDeclaration)
        super.visitFunctionDeclaration(functionDeclaration, compilationContext)
        compilationContext.popCompilationUnit()
    }

    override fun visitBasicBlock(bb: BasicBlock, compilationContext: CompilationContext) {
        super.visitBasicBlock(bb, compilationContext)
        val currentFunction = compilationContext.currentFunction()
        if (currentFunction.returnType.isVoid) {
            if (bb.expressions.isEmpty() || bb.expressions.last() !is ReturnExpr) {
                bb.appendExpression(ReturnExpr.voidReturn())
            }
        }
    }
}
