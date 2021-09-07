package org.jelik.parser.ast.branching;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWrapper;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IfConditionExpressionWrapperImpl extends ExpressionWrapper implements IfConditionExpression {

    public IfConditionExpressionWrapperImpl(@NotNull Expression expression) {
        super(expression);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
