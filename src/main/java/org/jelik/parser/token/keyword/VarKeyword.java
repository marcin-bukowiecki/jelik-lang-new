package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class VarKeyword extends Token {
    public VarKeyword(int row, int col) {
        super("var", row, col, ElementType.varKeyword);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }

    @Override
    public String toString() {
        return "var";
    }
}