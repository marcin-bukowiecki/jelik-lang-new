package org.jelik.parser.ast.types;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class GenericTypeNode extends TypeNode {

    @Getter
    private final SingleTypeNode singleTypeNode;

    @Getter
    private final List<TypeNode> typeVariables;

    public GenericTypeNode(@NotNull SingleTypeNode singleTypeNode, @NotNull List<TypeNode> typeVariables) {
        this.singleTypeNode = singleTypeNode;
        this.typeVariables = typeVariables;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return singleTypeNode.toString() + "<" + typeVariables.stream().map(Object::toString).collect(Collectors.joining(",")) + ">";
    }

    @Override
    public Type getType() {
        return singleTypeNode.getType();
    }

    @Override
    public Type getGenericType() {
        var type = singleTypeNode.getType();
        return new Type(
                type.getName(),
                type.getCanonicalName(),
                type.getTypeEnum(),
                type.getTypeParameters(),
                typeVariables.stream().map(TypeNode::getType).collect(Collectors.toList())
        );
    }

    @Override
    public String getSymbol() {
        return singleTypeNode.getSymbol();
    }
}
