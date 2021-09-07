package org.jelik.parser.token.keyword;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents package keyword
 *
 * @author Marcin Bukowiecki
 */
public class PackageKeyword extends Token {

    public PackageKeyword(int offset) {
        super("package", offset, ElementType.packageKeyword);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitPackageKeyword(this, parseContext);
    }
}
