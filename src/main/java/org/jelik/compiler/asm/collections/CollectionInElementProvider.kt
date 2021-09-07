package org.jelik.compiler.asm.collections

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.parser.ast.operators.InExpr
import org.jelik.parser.ast.resolvers.CastToVisitor

/**
 * @author Marcin Bukowiecki
 */
class CollectionInElementProvider(private val inExpr: InExpr): InElementProvider {

    override fun visit(toByteCodeVisitor: ToByteCodeVisitor, compilationContext: CompilationContext) {
        inExpr.right.accept(toByteCodeVisitor, compilationContext)
        val mv = toByteCodeVisitor.classWriterAdapter.currentMethodVisitor
        if (inExpr.left.genericReturnType.isPrimitive) {
            val wt = inExpr.left.genericReturnType.wrapperType
            inExpr.left.genericReturnType.accept(CastToVisitor(inExpr.left, wt), compilationContext)
        }
        inExpr.left.accept(toByteCodeVisitor, compilationContext)
        mv.invokeInstance(
                inExpr.right.genericReturnType.internalName,
        "contains",
                "(Ljava/lang/Object;)Z",
                inExpr.right.genericReturnType.isInterface
        )
    }
}
