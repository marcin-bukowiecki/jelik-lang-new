package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class VarKeyword extends Token implements Modifier {

    public VarKeyword(int offset) {
        super("var", offset, ElementType.varKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitVarKeyword(this, parseContext);
    }

    @Override
    public String toString() {
        return "var";
    }
}
