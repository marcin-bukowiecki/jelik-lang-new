package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ValKeyword extends Token implements Modifier {

    public ValKeyword(int row, int col) {
        super("val", row, col, ElementType.valKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitValKeyword(this, parseContext);
    }

    @Override
    public String toString() {
        return "val";
    }
}
