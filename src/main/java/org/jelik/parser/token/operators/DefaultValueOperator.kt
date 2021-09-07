package org.jelik.parser.token.operators

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.operators.AbstractOpExpr
import org.jelik.parser.ast.operators.DefaultValueExpr
import org.jelik.parser.token.ElementType

/**
 * Default operator ?:
 *
 * @author Marcin Bukowiecki
 */
class DefaultValueOperator(offset: Int) : AbstractOperator("?:", offset, ElementType.defaultValue) {

    override fun accept(tokenVisitor: TokenVisitor<*>, parseContext: ParseContext) {
        tokenVisitor.visitDefaultValueOperator(this, parseContext);
    }

    override fun toAst(left: Expression?, right: Expression?): AbstractOpExpr {
        return DefaultValueExpr(left, this, right)
    }
}
