package org.jelik.compiler.asm.slice

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.parser.ast.arrays.ArrayGetElementProvider
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr
import org.jelik.types.JVMIntType

/**
 * @author Marcin Bukowiecki
 */
class ArrayGetSliceProvider(arrayOrMapGetExpr: ArrayOrMapGetExpr) : ArrayGetElementProvider(arrayOrMapGetExpr) {

    override fun getElement(byteCodeVisitor: ToByteCodeVisitor, mv: MethodVisitorAdapter) {
        arrayOrMapGetExpr.leftExpr.accept(byteCodeVisitor, mv.compilationContext)
        arrayOrMapGetExpr.expression.accept(byteCodeVisitor, mv.compilationContext)
        val arrayType = arrayOrMapGetExpr.leftExpr.genericType

        val targetCallTypeName = if (!arrayType.getInnerType(0).isPrimitive) {
            ArrayPredefineSlice.predefineForType(arrayType, byteCodeVisitor, mv.compilationContext).internalName
        } else {
            "jelik/lang/JelikUtils"
        }

        val desc = arrayType.descriptor
        mv.invokeStatic(
                targetCallTypeName,
                "arraySlice",
                "(${desc}II)${desc}",
                arrayType,
                listOf(arrayType, JVMIntType.INSTANCE, JVMIntType.INSTANCE),
                false
        )
    }
}
