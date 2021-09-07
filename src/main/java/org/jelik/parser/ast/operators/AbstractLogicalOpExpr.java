package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jelik.types.JVMBooleanType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public abstract class AbstractLogicalOpExpr extends AbstractOpExpr {

    public LabelNode startLabel;

    public LabelNode trueLabelNode;

    public LabelNode falseLabelNode;

    public JumpInstruction instructionToCall;

    public AbstractLogicalOpExpr(Expression left, AbstractOperator op, Expression right) {
        super(left, op, right);
        this.nodeContext.setGenericType(JVMBooleanType.INSTANCE);
        this.nodeContext.setType(JVMBooleanType.INSTANCE);
    }

    @Override
    public abstract void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext);

    public boolean isNegated() {
        return falseLabelNode != null;
    }

    public boolean isLeft() {
        return getParent() instanceof AbstractOpExpr && ((AbstractOpExpr) getParent()).getLeft() == this;
    }

    public boolean isRight() {
        return getParent() instanceof AbstractOpExpr && ((AbstractOpExpr) getParent()).getRight() == this;
    }
}
