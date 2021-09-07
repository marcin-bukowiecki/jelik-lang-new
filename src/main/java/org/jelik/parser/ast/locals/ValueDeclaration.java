package org.jelik.parser.ast.locals;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.Statement;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWrapper;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.ValKeyword;
import org.jelik.parser.token.operators.AssignOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Marcin Bukowiecki
 */
public class ValueDeclaration extends ExpressionWrapper implements Statement, ValueOrVariableDeclaration {

    private final ValKeyword valKeyword;

    private final LiteralToken literalToken;

    private final TypeNode typeNode;

    private final AssignOperator assignOperator;

    private LocalVariable localVariable;

    public ValueDeclaration(@NotNull ValKeyword valKeyword,
                            @NotNull LiteralToken literalToken,
                            @Nullable TypeNode typeNode,
                            @Nullable AssignOperator assignOperator,
                            @NotNull Expression expression) {
        super(expression);
        this.valKeyword = valKeyword;
        this.literalToken = literalToken;
        this.typeNode = typeNode;
        this.assignOperator = assignOperator;
    }

    @NotNull
    public ValKeyword getValKeyword() {
        return valKeyword;
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
    public void setLocalVariable(@NotNull LocalVariable localVariable) {
        this.localVariable = localVariable;
    }

    public LocalVariable getLocalVariable() {
        return localVariable;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitValueDeclaration(this, compilationContext);
    }

    @Override
    public String toString() {
        return valKeyword.toString() + " " +
                literalToken.toString() + " " +
                (typeNode != null ? typeNode.toString() : "") +
                (assignOperator != null ? assignOperator.toString() : "") + getExpression().toString();
    }

    @Override
    public int getStartOffset() {
        return valKeyword.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return getExpression().getEndOffset();
    }
}
