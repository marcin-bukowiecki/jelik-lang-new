package org.jelik.parser.ast.types;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.InterfaceType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class GenericTypeNode extends TypeNode {

    @Getter
    private final TypeNode singleTypeNode;

    private final TypeVariableListNode typeVariables;

    public GenericTypeNode(@NotNull TypeNode singleTypeNode, @NotNull TypeVariableListNode typeVariables) {
        this.singleTypeNode = singleTypeNode;
        this.typeVariables = typeVariables;
    }

    public TypeVariableListNode getTypeVariables() {
        return typeVariables;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public int getStartCol() {
        return singleTypeNode.getStartCol();
    }

    @Override
    public int getStartRow() {
        return singleTypeNode.getStartRow();
    }

    @Override
    public int getEndCol() {
        return typeVariables.getEndCol();
    }

    @Override
    public int getEndRow() {
        return typeVariables.getEndRow();
    }

    @Override
    public String toString() {
        return singleTypeNode.toString() + typeVariables.toString();
    }

    @Override
    public Type getType() {
        return singleTypeNode.getType();
    }

    @Override
    public Type getGenericType() {
        var type = singleTypeNode.getType();
        if (type.isInterface()) {
            return new InterfaceType(
                    type.getName(),
                    type.getCanonicalName(),
                    type.getTypeEnum(),
                    type.getTypeParameters(),
                    typeVariables.getTypes().stream().map(TypeNode::getType).collect(Collectors.toList())
            );
        } else {
            return new Type(
                    type.getName(),
                    type.getCanonicalName(),
                    type.getTypeEnum(),
                    type.getTypeParameters(),
                    typeVariables.getTypes().stream().map(TypeNode::getType).collect(Collectors.toList())
            );
        }
    }

    @Override
    public String getSymbol() {
        return singleTypeNode.getSymbol();
    }
}
