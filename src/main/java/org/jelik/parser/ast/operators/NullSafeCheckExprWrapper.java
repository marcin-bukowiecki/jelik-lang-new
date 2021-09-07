package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.ConsumingExpression;
import org.jelik.parser.ast.NullExpr;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class NullSafeCheckExprWrapper extends EqualExpr implements ConsumingExpression {

    private final LabelNode endLabel;

    public NullSafeCheckExprWrapper(Expression left, AbstractOperator abstractOperator, LabelNode endLabel) {
        super(left, abstractOperator, new NullExpr(true));
        this.endLabel = endLabel;
    }

    public LabelNode getEndLabel() {
        return endLabel;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitNullSafeBooleanExprWrapper(this, compilationContext);
    }

    @Override
    public @NotNull List<ASTNode> getChildren() {
        return Collections.singletonList(left);
    }

    @Override
    public int getStartOffset() {
        return left.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return left.getEndOffset();
    }

    @Override
    public String toString() {
        return left.toString() + op.toString() + right.toString();
    }
}
