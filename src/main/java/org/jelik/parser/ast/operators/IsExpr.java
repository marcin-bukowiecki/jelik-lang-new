package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.IsOperator;
import org.jelik.types.JVMBooleanType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class IsExpr extends AbstractOpExpr {

    public IsExpr(Expression left, IsOperator addOperator, Expression right) {
        super(left, addOperator, right);
        this.nodeContext.setType(JVMBooleanType.INSTANCE);
        this.nodeContext.setGenericType(JVMBooleanType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
