package org.jelik.parser.token.keyword

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.ParseVisitor
import org.jelik.parser.token.ElementType
import org.jelik.parser.token.Token

/**
 * Represents throw keyword
 *
 * @author Marcin Bukowiecki
 */
class CatchKeyword(row: Int, col: Int) : Token("catch", row, col, ElementType.catchKeyword) {

    override fun visit(parseVisitor: ParseVisitor<*>, parseContext: ParseContext) {
        parseVisitor.visit(this, parseContext)
    }
}
