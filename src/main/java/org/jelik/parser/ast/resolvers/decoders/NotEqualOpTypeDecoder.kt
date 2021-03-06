package org.jelik.parser.ast.resolvers.decoders

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.runtime.TypeEnum
import org.jelik.compiler.exceptions.CompileException
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.NullExpr
import org.jelik.parser.ast.operators.JumpInstruction
import org.jelik.parser.ast.operators.NotEqualExpr
import org.jelik.parser.ast.resolvers.CastToVisitor
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
object NotEqualOpTypeDecoder {

    fun decode(op: NotEqualExpr, ctx: CompilationContext) {
        val leftType = op.left.genericReturnType
        val rightType = op.right.genericReturnType
        decode(leftType, rightType, op.left, op, op.right, ctx)
    }

    fun decode(leftType: Type, rightType: Type, leftCaller: Expression, op: NotEqualExpr, rightCaller: Expression, ctx: CompilationContext) {
        when(leftType.typeEnum) {
            TypeEnum.booleanT -> {
                when(rightType.typeEnum) {
                    TypeEnum.booleanT -> {
                        op.instructionToCall = JumpInstruction.if_icmpne
                    }
                    else -> throw NotImplementedError()
                }
            }
            TypeEnum.int32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int32 -> {
                        op.instructionToCall = JumpInstruction.if_icmpne
                    }
                    TypeEnum.string, TypeEnum.objectT -> {
                        if (rightType.isWrapper) {
                            rightType.accept(CastToVisitor(rightCaller, rightType.primitiveType), ctx)
                        } else {
                            throw CompileException("Can't apply ${rightCaller.type} to '!=' operator with ${leftCaller.type} type", rightCaller, ctx.currentModule)
                        }
                    }
                    else -> throw NotImplementedError()
                }
            }
            TypeEnum.float32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.float32 -> {
                        op.instructionToCall = JumpInstruction.floatGreater
                    }
                    else -> throw NotImplementedError()
                }
            }
            TypeEnum.float64 -> {
                when(rightType.typeEnum) {
                    TypeEnum.float64 -> {
                        op.instructionToCall = JumpInstruction.dcmpg
                    }
                    else -> throw NotImplementedError()
                }
            }
            TypeEnum.string -> {
                when(rightType.typeEnum) {
                    TypeEnum.nullT -> {
                        (rightCaller as NullExpr).ignore = true
                        op.instructionToCall = JumpInstruction.isNotNull
                    }
                    else -> throw NotImplementedError()
                }
            }
            TypeEnum.objectT -> {
                when(rightType.typeEnum) {
                    TypeEnum.objectT -> {
                        op.instructionToCall = JumpInstruction.refs_not_equal
                    }
                    else -> throw NotImplementedError()
                }
            }
            TypeEnum.nullT -> {
                when(rightType.typeEnum) {
                    TypeEnum.string -> {
                        (leftCaller as NullExpr).ignore = true
                        op.instructionToCall = JumpInstruction.isNotNull
                    }
                    else -> throw NotImplementedError()
                }
            }
            else -> throw UnsupportedOperationException()
        }
    }
}
