package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Null literal for null reference type
 *
 * @author Marcin Bukowiecki
 */
public class NullToken extends Token {

    public NullToken() {
        super("Null", -1, ElementType.nullLiteral);
    }

    public NullToken(int offset) {
        super("Null", offset, ElementType.nullLiteral);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }
}
