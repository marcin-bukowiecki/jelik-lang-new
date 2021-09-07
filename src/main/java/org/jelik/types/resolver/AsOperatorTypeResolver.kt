package org.jelik.types.resolver

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.runtime.TypeEnum
import org.jelik.compiler.exceptions.TypeCompileException
import org.jelik.parser.ast.numbers.Float32ToFloat64Node
import org.jelik.parser.ast.numbers.Float32ToInt32Node
import org.jelik.parser.ast.numbers.Float32ToInt64Node
import org.jelik.parser.ast.numbers.Float64ToFloat32
import org.jelik.parser.ast.numbers.Float64ToInt32Node
import org.jelik.parser.ast.numbers.Float64ToInt64Node
import org.jelik.parser.ast.numbers.Int32ToCharNode
import org.jelik.parser.ast.numbers.Int32ToFloat64Node
import org.jelik.parser.ast.numbers.Int32ToInt16Node
import org.jelik.parser.ast.numbers.Int32ToInt64Node
import org.jelik.parser.ast.numbers.Int32ToInt8Node
import org.jelik.parser.ast.numbers.Int64ToFloat32Node
import org.jelik.parser.ast.numbers.Int64ToFloat64Node
import org.jelik.parser.ast.numbers.Int64ToInt32Node
import org.jelik.parser.ast.operators.AsExpr
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
object AsOperatorTypeResolver {

    fun resolve(asExpr: AsExpr, ctx: CompilationContext) {
        val left = asExpr.left
        val right = asExpr.right
        val lt = left.genericReturnType
        val rt = right.genericReturnType

        when (lt.typeEnum) {
            TypeEnum.int32 ->
                when (rt.typeEnum) {
                    TypeEnum.charT -> {
                        asExpr.parent.replaceWith(asExpr, Int32ToCharNode(left))
                    }
                    TypeEnum.int8 -> {
                        asExpr.parent.replaceWith(asExpr, Int32ToInt8Node(left))
                    }
                    TypeEnum.int16 -> {
                        asExpr.parent.replaceWith(asExpr, Int32ToInt16Node(left))
                    }
                    TypeEnum.int32 -> {}
                    TypeEnum.int64 -> {
                        asExpr.parent.replaceWith(asExpr, Int32ToInt64Node(left))
                    }
                    TypeEnum.float64 -> {
                        asExpr.parent.replaceWith(asExpr, Int32ToFloat64Node(left))
                    }
                    else -> throw createException(lt, rt, asExpr, ctx)
                }
            TypeEnum.int64 ->
                when (rt.typeEnum) {
                    TypeEnum.int32 -> {
                        asExpr.parent.replaceWith(asExpr, Int64ToInt32Node(left))
                    }
                    TypeEnum.int64 -> {}
                    TypeEnum.float32 -> {
                        asExpr.parent.replaceWith(asExpr, Int64ToFloat32Node(left))
                    }
                    TypeEnum.float64 -> {
                        asExpr.parent.replaceWith(asExpr, Int64ToFloat64Node(left))
                    }
                    else -> throw createException(lt, rt, asExpr, ctx)
                }
            TypeEnum.float32 ->
                when (rt.typeEnum) {
                    TypeEnum.float32 -> {}
                    TypeEnum.float64 -> {
                        asExpr.parent.replaceWith(asExpr, Float32ToFloat64Node(left))
                    }
                    TypeEnum.int32 -> {
                        asExpr.parent.replaceWith(asExpr, Float32ToInt32Node(left))
                    }
                    TypeEnum.int64 -> {
                        asExpr.parent.replaceWith(asExpr, Float32ToInt64Node(left))
                    }
                    else -> throw createException(lt, rt, asExpr, ctx)
                }
            TypeEnum.float64 ->
                when (rt.typeEnum) {
                    TypeEnum.float32 -> {
                        asExpr.parent.replaceWith(asExpr, Float64ToFloat32(left))
                    }
                    TypeEnum.float64 -> {}
                    TypeEnum.int32 -> {
                        asExpr.parent.replaceWith(asExpr, Float64ToInt32Node(left))
                    }
                    TypeEnum.int64 -> {
                        asExpr.parent.replaceWith(asExpr, Float64ToInt64Node(left))
                    }
                    else -> throw createException(lt, rt, asExpr, ctx)
                }
            TypeEnum.objectT ->
                when (rt.typeEnum) {
                    TypeEnum.objectT -> {}
                    else -> throw createException(lt, rt, asExpr, ctx)
                }
            TypeEnum.nullT ->
                when (rt.typeEnum) {
                    TypeEnum.int32Wrapper,
                    TypeEnum.objectT -> {}
                    else -> throw createException(lt, rt, asExpr, ctx)
                }
            else -> throw createException(lt, rt, asExpr, ctx)
        }
    }

    private fun createException(leftType: Type, rightType: Type, asExpr: AsExpr, compilationContext: CompilationContext): RuntimeException {
        return TypeCompileException("Operator 'as' can't be applied to " +
                leftType.forErrorMessage() +
                " and " +
                rightType.forErrorMessage(),
                asExpr,
                compilationContext.currentModule)
    }
}
