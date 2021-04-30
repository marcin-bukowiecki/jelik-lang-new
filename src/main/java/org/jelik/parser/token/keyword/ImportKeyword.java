package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

public class ImportKeyword extends Token {

    public ImportKeyword(int row, int col) {
        super("import", row, col, ElementType.importKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitImportKeyword(this, parseContext);
    }
}
