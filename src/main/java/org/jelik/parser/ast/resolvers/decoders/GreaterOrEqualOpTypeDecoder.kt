package org.jelik.parser.ast.resolvers.decoders

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.common.TypeEnum
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.operators.GreaterOrEqualExpr
import org.jelik.parser.ast.operators.JumpInstruction
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
object GreaterOrEqualOpTypeDecoder {

    fun decode(op: GreaterOrEqualExpr, ctx: CompilationContext) {
        val leftType = op.left.genericReturnType
        val rightType = op.right.genericReturnType
        decode(leftType, rightType, op.left, op, op.right, ctx)
    }

    fun decode(leftType: Type, rightType: Type, leftCaller: Expression, op: GreaterOrEqualExpr, rightCaller: Expression, ctx: CompilationContext) {
        when(leftType.typeEnum) {
            TypeEnum.int8,
            TypeEnum.int16,
            TypeEnum.int32 -> {
                when(rightType.typeEnum) {
                    TypeEnum.int8,
                    TypeEnum.int16,
                    TypeEnum.int32 -> {
                        op.instructionToCall = JumpInstruction.intGreaterOrEqual
                    }
                    else -> throw RuntimeException()
                }
            }
            else -> throw RuntimeException()
        }
    }
}
