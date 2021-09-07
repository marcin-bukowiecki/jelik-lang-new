package org.jelik.compiler.mir.visitor

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.mir.MIRFunction
import org.jelik.compiler.mir.MIRValue
import org.jelik.parser.ast.ReturnExpr
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.nullsafe.NullSafeCallExpr
import org.jelik.parser.ast.visitors.AstVisitor
import java.util.*

/**
 * @author Marcin Bukowiecki
 */
class ToMIRVisitor(val compilationContext: CompilationContext) : AstVisitor() {

    private val mirStack = LinkedList<MIRValue>()

    fun visit(functionDeclaration: FunctionDeclaration): MIRFunction {
        functionDeclaration
            .functionBody
            .basicBlock
            .expressions
            .forEach { expr -> expr.accept(this, compilationContext) }

        return MIRFunction(functionDeclaration, mirStack)
    }

    override fun visitNullSafeCall(nullSafeCall: NullSafeCallExpr, compilationContext: CompilationContext) {

        
    }

    override fun visitReturnExpr(re: ReturnExpr, compilationContext: CompilationContext) {
        /*
        if (!re.isEmpty()) {
            re.expression.get().accept(this, compilationContext)
            val last = mirStack.pop()
            val toMIRReturnVisitor = ToMIRReturnVisitor(last)
            re.genericReturnType.accept(toMIRReturnVisitor, compilationContext)
            mirStack.addLast(toMIRReturnVisitor.returnStatement!!)
        } else {
            mirStack.addLast(MIRVoidReturn())
        }*/
    }
}
