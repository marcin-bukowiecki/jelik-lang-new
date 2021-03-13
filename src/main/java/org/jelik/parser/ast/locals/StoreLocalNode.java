package org.jelik.parser.ast.locals;

import org.jelik.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.token.LiteralToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class StoreLocalNode extends ExpressionReferencingType implements WithLocalVariable {

    private final LiteralToken literalToken;

    private final LocalVariable localVariable;

    public StoreLocalNode(LiteralToken literalToken, LocalVariable localVariable) {
        this.literalToken = literalToken;
        this.localVariable = localVariable;
        this.typedRefNodeContext.setTypeRef(localVariable.getTypeRef());
    }

    public LocalVariable getLocalVariable() {
        return localVariable;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return literalToken.toString() + (furtherExpression != null ? furtherExpression.toString() : "");
    }

    @Override
    public int getStartCol() {
        return literalToken.getCol();
    }

    @Override
    public int getStartRow() {
        return literalToken.getRow();
    }

    @Override
    public int getEndCol() {
        return literalToken.getCol() + literalToken.getText().length();
    }

    @Override
    public int getEndRow() {
        return literalToken.getRow();
    }
}
