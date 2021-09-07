package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ReturnKeyword extends Token {

    public static final ReturnKeyword MOCK = new ReturnKeyword(-1);

    public ReturnKeyword(int offset) {
        super("ret", offset, ElementType.returnKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitReturnKeyword(this, parseContext);
    }
}
