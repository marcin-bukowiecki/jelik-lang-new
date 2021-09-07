package org.jelik.parser.ast.branching

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.Token
import org.jelik.parser.token.keyword.WhenKeyword
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class WhenExpressionImpl(private val whenKeyword: WhenKeyword,
                         private val subject: Expression,
                         private val leftCurlToken: Token,
                         private val expressions: List<WhenCaseExpression>,
                         private val elseExpression: ElseExpression,
                         private val rightCurlToken: Token) : ASTNodeImpl(), WhenExpression {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        TODO("Not yet implemented")
    }

    override fun getReturnType(): Type {
        TODO("Not yet implemented")
    }

    override fun getGenericReturnType(): Type {
        TODO("Not yet implemented")
    }

    override fun getType(): Type {
        TODO("Not yet implemented")
    }

    override fun getGenericType(): Type {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "" + whenKeyword + " " + subject + " " + leftCurlToken + " " +
                expressions.joinToString(separator = ", ") + ", " +
                elseExpression +
                rightCurlToken
    }
}