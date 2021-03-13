package org.jelik.parser.ast.resolvers.decoders

import org.jelik.CompilationContext
import org.jelik.compiler.common.TypeEnum
import org.jelik.parser.ast.Expression
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

    fun decode(leftType: Type, rightType: Type, leftCaller: Expression, op: GreaterExpr, rightCaller: Expression, ctx: CompilationContext) {
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
                                rightCaller.ignored = true
                                op.instructionToCall = JumpInstruction.isGreaterThanZero
                            }
                            leftCaller.isZero -> {
                                leftCaller.ignored = true
                                op.instructionToCall = JumpInstruction.isLessThanZero
                            }
                            else -> {
                                op.instructionToCall = JumpInstruction.if_icmpgt
                            }
                        }
                    }
                    TypeEnum.objectT -> {
                        if (rightType.isWrapper) {
                            rightType.visit(CastToVisitor(rightCaller, rightType.primitiveType), ctx)
                        }
                    }
                }
            }
            TypeEnum.int64 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int64 -> {
                        op.instructionToCall = JumpInstruction.lcmp
                    }
                }
            }
            TypeEnum.float32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int32 -> {
                        rightCaller.parent.replaceWith(rightCaller, Int32ToFloat32Node(rightCaller))
                        op.instructionToCall = JumpInstruction.floatGreater
                    }
                }
            }
        }
    }
}
