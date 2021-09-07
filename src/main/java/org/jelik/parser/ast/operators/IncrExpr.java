package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.token.operators.IncrOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IncrExpr extends AbstractOpExpr implements StackConsumer {

    public IncrExpr(IncrOperator incrOperator, Expression right) {
        super(incrOperator, right);
    }

    @Override
    public int getStartOffset() {
        return op.getStartOffset();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
