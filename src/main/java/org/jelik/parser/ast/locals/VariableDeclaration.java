package org.jelik.parser.ast.locals;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.Statement;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWrapper;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.VarKeyword;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Represents:
 *
 * var a = expr
 *
 * var a Int = expr
 *
 * @author Marcin Bukowiecki
 */
public class VariableDeclaration extends ExpressionWrapper implements Statement, ValueOrVariableDeclaration {

    private final VarKeyword varKeyword;

    private final LiteralToken literalToken;

    private final TypeNode typeNode;

    private final AssignOperator assignOperator;

    private LocalVariable localVariable;

    public VariableDeclaration(@NotNull VarKeyword varKeyword,
                               @NotNull LiteralToken literalToken,
                               @NotNull TypeNode typeNode,
                               @NotNull AssignOperator assignOperator,
                               @NotNull Expression expression) {
        super(expression);
        this.varKeyword = varKeyword;
        this.literalToken = literalToken;
        this.typeNode = typeNode;
        this.assignOperator = assignOperator;
    }

    @Override
    public void setLocalVariable(LocalVariable localVariable) {
        this.localVariable = localVariable;
    }

    @NotNull
    public VarKeyword getVarKeyword() {
        return varKeyword;
    }

    @NotNull
    @Override
    public LiteralToken getLiteralToken() {
        return literalToken;
    }

    @NotNull
    @Override
    public TypeNode getTypeNode() {
        return typeNode;
    }

    @NotNull
    public AssignOperator getAssignOperator() {
        return assignOperator;
    }

    @Override
    public LocalVariable getLocalVariable() {
        return localVariable;
    }

    @Override
    public int getStartOffset() {
        return varKeyword.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return getExpression().getEndOffset();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitVariableDeclaration(this, compilationContext);
    }

    @Override
    public String toString() {
        return varKeyword.toString() + " " +
                literalToken.toString() + " " +
                (typeNode instanceof UndefinedTypeNode ? "" : typeNode.toString() + " ") +
                assignOperator.toString() + " " + getExpression();
    }
}
