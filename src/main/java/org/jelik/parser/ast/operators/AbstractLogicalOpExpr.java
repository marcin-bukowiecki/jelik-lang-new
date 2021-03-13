package org.jelik.parser.ast.operators;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
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
    public abstract void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext);

    @Override
    public boolean isLogical() {
        return true;
    }

    public boolean isNegated() {
        return falseLabelNode != null;
    }

    public boolean isLeft() {
        return parent instanceof AbstractOpExpr && ((AbstractOpExpr) parent).getLeft() == this;
    }

    public boolean isRight() {
        return parent instanceof AbstractOpExpr && ((AbstractOpExpr) parent).getRight() == this;
    }
}
