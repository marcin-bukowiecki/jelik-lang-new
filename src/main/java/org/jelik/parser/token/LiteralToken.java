package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class LiteralToken extends Token {

    public LiteralToken(int offset, String text) {
        super(text, offset, ElementType.literal);
    }

    public LiteralToken(String text) {
        super(text, -1, ElementType.literal);
    }

    public LiteralToken(int offset, String text, ElementType empty) {
        super(text, offset, empty);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitLiteral(this, parseContext);
    }
}
