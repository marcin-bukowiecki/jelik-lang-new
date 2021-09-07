package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * True literal for Boolean type
 *
 * @author Marcin Bukowiecki
 */
public class TrueToken extends Token {

    public TrueToken(int offset) {
        super("True", offset, ElementType.trueLiteral);
    }

    public TrueToken() {
        this(-1);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitTrue(this, parseContext);
    }
}
