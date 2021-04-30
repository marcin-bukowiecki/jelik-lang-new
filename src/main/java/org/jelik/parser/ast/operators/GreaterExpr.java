package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.token.operators.GreaterOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class GreaterExpr extends AbstractLogicalOpExpr {

    public GreaterExpr(Expression left, GreaterOperator addOperator, Expression right) {
        super(left, addOperator, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
