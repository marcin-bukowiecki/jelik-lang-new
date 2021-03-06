package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.PackageKeyword;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class PackageDeclaration extends ASTNodeImpl {

    private final PackageKeyword packageKeyword;

    private final List<Token> tokens;

    public PackageDeclaration(PackageKeyword packageKeyword, List<Token> tokens) {
        this.packageKeyword = packageKeyword;
        this.tokens = tokens;
    }

    @Override
    public int getStartOffset() {
        return packageKeyword.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return tokens.get(tokens.size() - 1).getEndOffset();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }

    public boolean isDefault() {
        return tokens == null || tokens.size() == 0;
    }

    public String getPrettyPath() {
        return tokens.stream().map(Token::toString).collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return packageKeyword.toString() + " " + getPrettyPath();
    }
}
