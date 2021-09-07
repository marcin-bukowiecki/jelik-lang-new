package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class LiteralExpr extends ASTNodeImpl implements Expression {

    private final LiteralToken literalToken;

    private TypeVariableListNode typeParameterListNode = TypeVariableListNode.Companion.getEMPTY();

    public LiteralExpr(LiteralToken literalToken) {
        this.literalToken = literalToken;
    }

    public void setTypeParameterListNode(TypeVariableListNode typeParameterListNode) {
        this.typeParameterListNode = typeParameterListNode;
    }

    public TypeVariableListNode getTypeParameterListNode() {
        return typeParameterListNode;
    }

    @Override
    public int getStartOffset() {
        return literalToken.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return literalToken.getEndOffset();
    }

    public LiteralToken getLiteralToken() {
        return literalToken;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public Type getReturnType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public Type getGenericReturnType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }

    @Override
    public Type getGenericType() {
        throw new UnsupportedOperationException("Literal was not resolved");
    }


    public String getText() {
        return literalToken.getText();
    }

    @Override
    public String toString() {
        return literalToken
                + (typeParameterListNode == TypeVariableListNode.Companion.getEMPTY() ? "" :
                typeParameterListNode.toString());
    }
}
