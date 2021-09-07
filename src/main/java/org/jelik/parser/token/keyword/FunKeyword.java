package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunKeyword extends Token {

    public static final FunKeyword DUMMY = new FunKeyword(-1);

    public FunKeyword(int offset) {
        super("fun", offset, ElementType.funKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitFunKeyword(this, parseContext);
    }
}
