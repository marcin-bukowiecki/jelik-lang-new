package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class EofTok extends Token {

    public EofTok(int offset) {
        super("eof", offset, ElementType.eof);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }

    @Override
    public String toString() {
        return "EOF";
    }
}
