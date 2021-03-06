package org.jelik.parser.ast.functions;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionParameter extends ASTNodeImpl {

    private final TypeNode typeNode;

    private final LiteralToken name;

    private final Token comma;

    public LocalVariable localVariableRef;

    public FunctionParameter(@NotNull TypeNode typeNode, LiteralToken name, Token comma) {
        this.typeNode = typeNode;
        this.name = name;
        this.comma = comma;
        typeNode.setParent(this);
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Optional<Token> getComma() {
        return Optional.ofNullable(comma);
    }

    public LiteralToken getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString() + " " + typeNode.toString() + (comma == null ? "" : ",");
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    public boolean isInferred() {
        return false;
    }
}
