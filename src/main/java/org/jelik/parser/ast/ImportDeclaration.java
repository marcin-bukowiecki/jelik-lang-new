package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
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

    private final List<Token> tokens;

    public ImportDeclaration(ImportKeyword importKeyword, List<Token> tokens) {
        this.importKeyword = importKeyword;
        this.tokens = tokens;
    }

    @NotNull
    public List<Token> getTokens() {
        return tokens;
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
    public int getEndOffset() {
        return tokens.get(tokens.size() - 1).getEndOffset();
    }
}
