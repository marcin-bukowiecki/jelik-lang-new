package org.jelik.types.resolver

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.common.TypeEnum
import org.jelik.parser.ast.numbers.Int64ToInt32Node
import org.jelik.parser.ast.operators.AbstractOpExpr
import org.jelik.types.Type
import org.jelik.types.jvm.JVMLongType

/**
 * @author Marcin Bukowiecki
 */
open class BaseLongBitShifter : BaseOpTypeResolver() {

    override fun resolve(opExpr: AbstractOpExpr, compilationContext: CompilationContext): Type {
        val leftType = opExpr.left.genericReturnType
        val rightType = opExpr.right.genericReturnType

        return when (leftType.getTypeEnum()) {
            TypeEnum.int64 -> {
                when (rightType.getTypeEnum()) {
                    TypeEnum.int32 -> {
                        JVMLongType.INSTANCE
                    }
                    TypeEnum.int64 -> {
                        opExpr.right.parent.replaceWith(opExpr.right, Int64ToInt32Node(opExpr.right))
                        JVMLongType.INSTANCE
                    }
                    else -> super.resolve(opExpr, compilationContext)
                }
            }
            else -> super.resolve(opExpr, compilationContext)
        }
    }
}
