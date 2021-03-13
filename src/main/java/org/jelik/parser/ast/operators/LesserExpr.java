package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class LesserExpr extends AbstractLogicalOpExpr {

    public LesserExpr(Expression left, AbstractOperator addOperator, Expression right) {
        super(left, addOperator, right);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
