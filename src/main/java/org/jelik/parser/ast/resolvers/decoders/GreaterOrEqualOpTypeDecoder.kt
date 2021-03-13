package org.jelik.parser.ast.resolvers.decoders

import org.jelik.CompilationContext
import org.jelik.compiler.common.TypeEnum
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.numbers.CharToInt64Node
import org.jelik.parser.ast.numbers.Int32ToFloat32Node
import org.jelik.parser.ast.operators.EqualExpr
import org.jelik.parser.ast.operators.GreaterExpr
import org.jelik.parser.ast.operators.GreaterOrEqualExpr
import org.jelik.parser.ast.operators.JumpInstruction
import org.jelik.parser.ast.resolvers.CastToVisitor
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
