package org.jelik.parser.token.operators

import org.jelik.parser.ParseContext
import org.jelik.parser.ast.TokenVisitor
import org.jelik.parser.token.ElementType
import org.jelik.parser.token.Token

/**
 * Default operator ?:
 *
 * @author Marcin Bukowiecki
 */
class DefaultValueOperator(row: Int, col: Int) : Token("?:", row, col, ElementType.defaultValue) {

    override fun accept(tokenVisitor: TokenVisitor<*>, parseContext: ParseContext) {
        tokenVisitor.visitDefaultValueOperator(this, parseContext);
    }
}
