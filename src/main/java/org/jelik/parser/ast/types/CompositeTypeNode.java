package org.jelik.parser.ast.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class CompositeTypeNode extends TypeNode {

    private final List<LiteralToken> paths;

    private final TypeNode typeNode;

    public CompositeTypeNode(@NotNull List<LiteralToken> paths, @NotNull TypeNode typeNode) {
        this.paths = paths;
        this.typeNode = typeNode;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public @NotNull List<@NotNull LiteralToken> getPaths() {
        return paths;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitCompositeTypeNode(this, compilationContext);
    }

    @Override
    public String getSymbol() {
        return paths
                .stream()
                .map(Token::toString).collect(Collectors.joining(".")) + "." + typeNode;
    }

    @Override
    public String toString() {
        return getSymbol();
    }

    @Override
    public int getStartOffset() {
        return paths.get(0).getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return typeNode.getEndOffset();
    }
}
