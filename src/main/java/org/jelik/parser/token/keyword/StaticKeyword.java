package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class StaticKeyword extends Token {

    public StaticKeyword(int offset) {
        super("static", offset, ElementType.staticKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitStaticKeyword(this, parseContext);
    }

    @Override
    public String toString() {
        return "static";
    }
}
