package org.jelik.parser.ast;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ImportKeyword;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class ImportDeclaration extends TypeNode {

    private final ImportKeyword importKeyword;

    @Getter
    private final List<Token> tokens;

    public ImportDeclaration(ImportKeyword importKeyword, List<Token> tokens) {
        this.importKeyword = importKeyword;
        this.tokens = tokens;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitImportDeclaration(this, compilationContext);
    }

    public String canonicalPath() {
        return tokens.stream().map(Token::toString).collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return importKeyword.toString() + " " + tokens.stream().map(Token::toString).collect(Collectors.joining());
    }

    @Override
    public String getSymbol() {
        return toString();
    }

    @Override
    public int getEndRow() {
        return tokens.get(tokens.size()-1).getEndRow();
    }
}
