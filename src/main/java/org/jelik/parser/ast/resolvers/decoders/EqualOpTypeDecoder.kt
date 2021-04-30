package org.jelik.parser.ast.resolvers.decoders

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.common.TypeEnum
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.NullExpr
import org.jelik.parser.ast.operators.EqualExpr
import org.jelik.parser.ast.operators.JumpInstruction
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
object EqualOpTypeDecoder {

    fun decode(op: EqualExpr, ctx: CompilationContext) {
        val leftType = op.left.genericReturnType
        val rightType = op.right.genericReturnType
        decode(leftType, rightType, op.left, op, op.right, ctx)
    }

    fun decode(leftType: Type, rightType: Type, leftCaller: Expression, op: EqualExpr, rightCaller: Expression, ctx: CompilationContext) {
        when(leftType.typeEnum) {
            TypeEnum.booleanT -> {
                when(rightType.typeEnum) {
                    TypeEnum.booleanT -> {
                        op.instructionToCall = JumpInstruction.if_icmpeq
                    }
                }
            }
            TypeEnum.int8,
            TypeEnum.int16,
            TypeEnum.int32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int8,
                    TypeEnum.int16,
                    TypeEnum.int32 -> {
                        op.instructionToCall = JumpInstruction.if_icmpeq
                    }
                    TypeEnum.objectT -> {
                        if (rightType.isWrapper) {
                            rightType.accept(CastToVisitor(rightCaller, rightType.primitiveType), ctx)
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
                    TypeEnum.float32 -> {
                        op.instructionToCall = JumpInstruction.floatLesser
                    }
                }
            }
            TypeEnum.float64 -> {
                when(rightType.typeEnum) {
                    TypeEnum.float64 -> {
                        op.instructionToCall = JumpInstruction.dcmpl
                    }
                }
            }
            TypeEnum.string -> {
                when(rightType.typeEnum) {
                    TypeEnum.string -> {
                        op.instructionToCall = JumpInstruction.refs_equal
                    }
                    TypeEnum.nullT -> {
                        (rightCaller as NullExpr).ignore = true
                        op.instructionToCall = JumpInstruction.isNull
                    }
                }
            }
            TypeEnum.objectT -> {
                when(rightType.typeEnum) {
                    TypeEnum.string -> {
                        op.instructionToCall = JumpInstruction.refs_equal
                    }
                    TypeEnum.objectT -> {
                        op.instructionToCall = JumpInstruction.refs_equal
                    }
                }
            }
            TypeEnum.nullT -> {
                when(rightType.typeEnum) {
                    TypeEnum.string -> {
                        (leftCaller as NullExpr).ignore = true
                        op.instructionToCall = JumpInstruction.isNull
                    }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}
