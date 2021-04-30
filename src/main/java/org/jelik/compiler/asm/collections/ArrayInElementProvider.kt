package org.jelik.compiler.asm.collections

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.asm.helpers.ArrayToStreamVisitor
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.parser.ast.operators.InExpr
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.types.Type
import java.util.stream.Collectors

/**
 * @author Marcin Bukowiecki
 */
class ArrayInElementProvider(private val inExpr: InExpr): InElementProvider {

    override fun visit(toByteCodeVisitor: ToByteCodeVisitor, compilationContext: CompilationContext) {
        val mv = toByteCodeVisitor.classWriterAdapter.currentMethodVisitor
        inExpr.right.accept(toByteCodeVisitor, compilationContext)
        inExpr.right.genericReturnType
                .getInnerType(0)
                .accept(ArrayToStreamVisitor(mv, inExpr.right), compilationContext)
        mv.invokeStatic(
                Type.of(Collectors::class.java).internalName,
                "toList",
                "()Ljava/util/stream/Collector;"
        )
        mv.invokeInstance(
                "java/util/stream/Stream",
                "collect",
                "(Ljava/util/stream/Collector;)Ljava/lang/Object;",
                true
        )
        mv.checkCast(Type.of(List::class.java))
        if (inExpr.left.genericReturnType.isPrimitive) {
            val wt = inExpr.left.genericReturnType.wrapperType
            inExpr.left.genericReturnType.accept(CastToVisitor(inExpr.left, wt), compilationContext)
        }
        inExpr.left.accept(toByteCodeVisitor, compilationContext)
        mv.invokeInstance(
                "java/util/List",
        "contains",
                "(Ljava/lang/Object;)Z",
                true
        )
    }
}
