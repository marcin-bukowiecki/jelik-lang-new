package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ClassKeyword extends Token {

    public ClassKeyword(int row, int col) {
        super("class", row, col, ElementType.classKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitClassKeyword(this, parseContext);
    }
}
