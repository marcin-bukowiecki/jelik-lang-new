package org.jelik.parser.token

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor

/**
 * Represents [
 *
 * @author Marcin Bukowiecki
 */
class RightBracketToken(offset: Int) : Token("]", offset, ElementType.rightBracket) {

    override fun accept(parseVisitor: TokenVisitor<*>, parseContext: ParseContext) {
        parseVisitor.visit(this, parseContext)
    }
}
