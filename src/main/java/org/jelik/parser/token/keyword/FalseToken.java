package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * False literal for Boolean type
 *
 * @author Marcin Bukowiecki
 */
public class FalseToken extends Token {

    public FalseToken() {
        super("False", -1, -1, ElementType.falseLiteral);
    }

    public FalseToken(int row, int col) {
        super("False", row, col, ElementType.falseLiteral);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext) {
        tokenVisitor.visitFalse(this, parseContext);
    }
}
