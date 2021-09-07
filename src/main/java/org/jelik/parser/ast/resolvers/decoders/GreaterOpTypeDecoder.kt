package org.jelik.parser.ast.resolvers.decoders

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.runtime.TypeEnum
import org.jelik.compiler.helper.CompilerHelper
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.numbers.Int32ToFloat32Node
import org.jelik.parser.ast.operators.GreaterExpr
import org.jelik.parser.ast.operators.JumpInstruction
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.types.Type

/**
 * Represents '>' operator
 *
 * @author Marcin Bukowiecki
 */
object GreaterOpTypeDecoder {

    fun decode(op: GreaterExpr, ctx: CompilationContext) {
        val leftType = op.left.genericReturnType
        val rightType = op.right.genericReturnType

        decode(leftType, rightType, op.left, op, op.right, ctx)
    }

    fun decode(leftType: Type,
               rightType: Type,
               leftCaller: Expression,
               op: GreaterExpr,
               rightCaller: Expression,
               ctx: CompilationContext
    ) {

        when(leftType.typeEnum) {
            TypeEnum.int8,
            TypeEnum.int16,
            TypeEnum.int32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int8,
                    TypeEnum.int16,
                    TypeEnum.int32 -> {
                        when {
                            rightCaller.isZero -> {
                                rightCaller.isIgnored = true
                                op.instructionToCall = JumpInstruction.isGreaterThanZero
                            }
                            leftCaller.isZero -> {
                                leftCaller.isIgnored = true
                                op.instructionToCall = JumpInstruction.isLessThanZero
                            }
                            else -> {
                                op.instructionToCall = JumpInstruction.if_icmpgt
                            }
                        }
                    }
                    TypeEnum.objectT -> {
                        if (rightType.isWrapper) {
                            rightType.accept(CastToVisitor(rightCaller, rightType.primitiveType), ctx)
                        }
                    }
                    else -> CompilerHelper.raiseTypeCompileError("operator.applyError", op, leftType, rightType)
                }
            }
            TypeEnum.int64 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int64 -> {
                        op.instructionToCall = JumpInstruction.lcmp
                    }
                    else -> CompilerHelper.raiseTypeCompileError("operator.applyError", op, leftType, rightType)
                }
            }
            TypeEnum.float32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int32 -> {
                        rightCaller.parent.replaceWith(rightCaller, Int32ToFloat32Node(rightCaller))
                        op.instructionToCall = JumpInstruction.floatGreater
                    }
                    else -> CompilerHelper.raiseTypeCompileError("operator.applyError", op, leftType, rightType)
                }
            }
            else -> CompilerHelper.raiseTypeCompileError("operator.applyError", op, leftType, rightType)
        }
    }
}
