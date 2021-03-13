package org.jelik.parser.token

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.ParseVisitor

/**
 * Represents [
 *
 * @author Marcin Bukowiecki
 */
class LeftBracketToken(row: Int, col: Int) : Token("[", row, col, ElementType.leftBracket) {

    override fun visit(parseVisitor: ParseVisitor<*>, parseContext: ParseContext) {
        parseVisitor.visit(this, parseContext)
    }
}