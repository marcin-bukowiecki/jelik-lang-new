package org.jelik.parser.ast.arrays

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.types.JVMObjectType
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class MapGetElementProvider(arrayOrMapGetExpr: ArrayOrMapGetExpr) : ArrayGetElementProvider(arrayOrMapGetExpr) {

    override fun getElement(byteCodeVisitor: ToByteCodeVisitor, mv: MethodVisitorAdapter) {
        val compilationContext = mv.compilationContext
        arrayOrMapGetExpr.leftExpr.accept(byteCodeVisitor, compilationContext)
        arrayOrMapGetExpr.expression.accept(byteCodeVisitor, compilationContext)
        mv.invokeInterface(Type.of(Map::class.java).internalName,
                "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                JVMObjectType.INSTANCE,
                listOf(JVMObjectType.INSTANCE)
        )
    }
}
