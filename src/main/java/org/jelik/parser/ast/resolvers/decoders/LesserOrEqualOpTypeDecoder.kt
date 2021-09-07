package org.jelik.parser.ast.resolvers.decoders

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.runtime.TypeEnum
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.operators.JumpInstruction
import org.jelik.parser.ast.operators.LesserOrEqualExpr
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
object LesserOrEqualOpTypeDecoder {

    fun decode(op: LesserOrEqualExpr, ctx: CompilationContext) {
        val leftType = op.left.genericReturnType
        val rightType = op.right.genericReturnType
        decode(leftType, rightType, op.left, op, op.right, ctx)
    }

    //TODO add support to other types
    fun decode(leftType: Type,
               rightType: Type,
               leftCaller: Expression,
               op: LesserOrEqualExpr,
               rightCaller: Expression,
               ctx: CompilationContext) {

        when(leftType.typeEnum) {
            TypeEnum.int8,
            TypeEnum.int16,
            TypeEnum.int32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int8,
                    TypeEnum.int16,
                    TypeEnum.int32 -> {
                        op.instructionToCall = JumpInstruction.intLesserOrEqual
                    }
                    else -> throw RuntimeException()
                }
            }
            else -> throw RuntimeException()
        }
    }
}
