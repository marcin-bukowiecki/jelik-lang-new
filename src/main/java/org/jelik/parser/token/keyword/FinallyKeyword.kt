package org.jelik.parser.token.keyword

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.token.ElementType
import org.jelik.parser.token.Token

/**
 * Represents throw keyword
 *
 * @author Marcin Bukowiecki
 */
class FinallyKeyword(row: Int, col: Int) : Token("finally", row, col, ElementType.finallyKeyword) {

    override fun accept(parseVisitor: TokenVisitor<*>, parseContext: ParseContext) {
        parseVisitor.visit(this, parseContext)
    }
}
