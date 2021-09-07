package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class WhileKeyword extends Token {

    public WhileKeyword(int offset) {
        super("while", offset, ElementType.whileKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitWhileKeyword(this, parseContext);
    }
}
