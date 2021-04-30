package org.jelik.parser.ast.arrays

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ByteCodeArrayGetVisitor
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.types.JVMIntType

/**
 * @author Marcin Bukowiecki
 */
open class ArrayGetElementProvider(protected val arrayOrMapGetExpr: ArrayOrMapGetExpr) {

    open fun getElement(byteCodeVisitor: ToByteCodeVisitor, mv: MethodVisitorAdapter) {
        arrayOrMapGetExpr.leftExpr.accept(byteCodeVisitor, mv.compilationContext)
        mv.dup()
        arrayOrMapGetExpr.expression.accept(byteCodeVisitor, mv.compilationContext)
        val arrayType = arrayOrMapGetExpr.leftExpr.genericType
        val desc = arrayType.descriptor
        mv.invokeStatic(
                "jelik/lang/JelikUtils",
                "calculateArrayIndex",
                "(${desc}I)I",
                JVMIntType.INSTANCE,
                listOf(arrayType, JVMIntType.INSTANCE),
                false
        )
        arrayOrMapGetExpr.type.accept(ByteCodeArrayGetVisitor(mv), mv.compilationContext)
    }
}
