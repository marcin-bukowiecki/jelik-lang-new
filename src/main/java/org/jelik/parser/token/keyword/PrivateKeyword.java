package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class PrivateKeyword extends Token implements Modifier {

    public PrivateKeyword(int row, int col) {
        super("private", row, col, ElementType.privateKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> tokenVisitor, @NotNull ParseContext parseContext) {
        tokenVisitor.visitPrivateKeyword(this, parseContext);
    }
}
