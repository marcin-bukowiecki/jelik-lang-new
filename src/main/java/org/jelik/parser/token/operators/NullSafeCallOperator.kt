package org.jelik.parser.token.operators

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.token.ElementType
import org.jelik.parser.token.Token

/**
 * Null safe operator ?.
 *
 * @author Marcin Bukowiecki
 */
class NullSafeCallOperator(offset: Int) : Token("?.", offset, ElementType.nullSafeCall) {

    override fun accept(tokenVisitor: TokenVisitor<*>, parseContext: ParseContext) {
        tokenVisitor.visitNullSafeCallOperator(this, parseContext);
    }
}
