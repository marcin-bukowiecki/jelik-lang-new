package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents .
 *
 * @author Marcin Bukowiecki
 */
public class DotToken extends Token {

    public DotToken(int offset) {
        super(".", offset, ElementType.dot);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitDot(this, parseContext);
    }
}
