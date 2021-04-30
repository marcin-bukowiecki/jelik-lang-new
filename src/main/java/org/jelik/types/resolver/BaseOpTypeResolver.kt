package org.jelik.types.resolver

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.common.TypeEnum
import org.jelik.compiler.exceptions.TypeCompileException
import org.jelik.parser.ast.numbers.CharToInt64Node
import org.jelik.parser.ast.numbers.Float32ToFloat64Node
import org.jelik.parser.ast.numbers.Int32ToFloat32Node
import org.jelik.parser.ast.numbers.Int32ToFloat64Node
import org.jelik.parser.ast.numbers.Int32ToInt64Node
import org.jelik.parser.ast.numbers.IntegerWrapperToInt32Node
import org.jelik.parser.ast.operators.AbstractOpExpr
import org.jelik.types.JVMIntType
import org.jelik.types.Type
import org.jelik.types.jvm.JVMDoubleType
import org.jelik.types.jvm.JVMFloatType
import org.jelik.types.jvm.JVMLongType

/**
 * @author Marcin Bukowiecki
 */
abstract class BaseOpTypeResolver {

    open fun resolve(opExpr: AbstractOpExpr, compilationContext: CompilationContext): Type {
        val leftType = opExpr.left.genericReturnType
        val rightType = opExpr.right.genericReturnType

        return when (leftType.getTypeEnum()) {
            TypeEnum.charT -> when (rightType.getTypeEnum()) {
                TypeEnum.int32 -> {
                    JVMIntType.INSTANCE
                }
                TypeEnum.int64 -> {
                    opExpr.left.parent.replaceWith(opExpr.left, CharToInt64Node(opExpr.left))
                    JVMLongType.INSTANCE
                }
                else -> throw createException(leftType, rightType, opExpr, compilationContext)
            }
            TypeEnum.int32Wrapper -> when (rightType.getTypeEnum()) {
                TypeEnum.int32Wrapper -> {
                    opExpr.left.parent.replaceWith(opExpr.left, IntegerWrapperToInt32Node(opExpr.left))
                    opExpr.right.parent.replaceWith(opExpr.right, IntegerWrapperToInt32Node(opExpr.right))
                    JVMIntType.INSTANCE
                }
                TypeEnum.int32 -> {
                    opExpr.left.parent.replaceWith(opExpr.left, IntegerWrapperToInt32Node(opExpr.left))
                    JVMIntType.INSTANCE
                }
                else -> throw createException(leftType, rightType, opExpr, compilationContext)
            }
            TypeEnum.int8,
            TypeEnum.int16,
            TypeEnum.int32 -> when (rightType.getTypeEnum()) {
                TypeEnum.int32Wrapper -> {
                    opExpr.right.parent.replaceWith(opExpr.right, IntegerWrapperToInt32Node(opExpr.right))
                    JVMIntType.INSTANCE
                }
                TypeEnum.int8,
                TypeEnum.int16,
                TypeEnum.int32 -> JVMIntType.INSTANCE
                TypeEnum.int64 -> {
                    opExpr.left.parent.replaceWith(opExpr.left, Int32ToInt64Node(opExpr.left))
                    JVMLongType.INSTANCE
                }
                else -> throw createException(leftType, rightType, opExpr, compilationContext)
            }
            TypeEnum.int64 -> when (rightType.getTypeEnum()) {
                TypeEnum.int32 -> {
                    opExpr.right.parent.replaceWith(opExpr.right, Int32ToInt64Node(opExpr.right))
                    JVMLongType.INSTANCE
                }
                TypeEnum.int64 -> JVMLongType.INSTANCE
                else -> throw createException(leftType, rightType, opExpr, compilationContext)
            }
            TypeEnum.float32 -> when (rightType.getTypeEnum()) {
                TypeEnum.int32 -> {
                    opExpr.right.parent.replaceWith(opExpr.right, Int32ToFloat32Node(opExpr.right))
                    JVMFloatType.INSTANCE
                }
                TypeEnum.float32 -> JVMFloatType.INSTANCE
                else -> throw createException(leftType, rightType, opExpr, compilationContext)
            }
            TypeEnum.float64 -> when (rightType.getTypeEnum()) {
                TypeEnum.int32 -> {
                    opExpr.right.parent.replaceWith(opExpr.right, Int32ToFloat64Node(opExpr.right))
                    JVMDoubleType.INSTANCE
                }
                TypeEnum.float32 -> {
                    opExpr.right.parent.replaceWith(opExpr.right, Float32ToFloat64Node(opExpr.right))
                    JVMDoubleType.INSTANCE
                }
                TypeEnum.float64 -> JVMDoubleType.INSTANCE
                else -> throw createException(leftType, rightType, opExpr, compilationContext)
            }
            else -> throw createException(leftType, rightType, opExpr, compilationContext)
        }
    }

    open fun createException(leftType: Type, rightType: Type, expr: AbstractOpExpr, compilationContext: CompilationContext): RuntimeException {
        return TypeCompileException("Operator '${expr.op.text}' can't be applied to " +
                leftType.forErrorMessage() +
                " and " +
                rightType.forErrorMessage(),
                expr,
                compilationContext.currentModule)
    }
}
