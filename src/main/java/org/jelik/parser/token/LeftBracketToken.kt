package org.jelik.parser.token

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor

/**
 * Represents [
 *
 * @author Marcin Bukowiecki
 */
class LeftBracketToken(row: Int, col: Int) : Token("[", row, col, ElementType.leftBracket) {

    override fun accept(parseVisitor: TokenVisitor<*>, parseContext: ParseContext) {
        parseVisitor.visit(this, parseContext)
    }
}
