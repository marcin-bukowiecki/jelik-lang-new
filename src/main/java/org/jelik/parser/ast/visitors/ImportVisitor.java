package org.jelik.parser.ast.visitors;

import com.google.common.collect.Lists;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ImportKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * Represents Import expression: import foo.bar
 *
 * @author Marcin Bukowiecki
 */
public class ImportVisitor implements TokenVisitor<ImportDeclaration> {

    private final ImportKeyword importKeyword;

    public ImportVisitor(@NotNull ImportKeyword importKeyword) {
        this.importKeyword = importKeyword;
    }

    @Override
    @NotNull
    public ImportDeclaration visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        var acc = Lists.<Token>newArrayList();

        while (lexer.hasNextToken()) {
            var peekedNext = lexer.peekNext();
            if (peekedNext.getTokenType() != ElementType.literal && peekedNext.getTokenType() != ElementType.dot) {
                break;
            }
            acc.add(peekedNext);
            lexer.nextToken();
        }

        return new ImportDeclaration(importKeyword, acc);
    }
}
