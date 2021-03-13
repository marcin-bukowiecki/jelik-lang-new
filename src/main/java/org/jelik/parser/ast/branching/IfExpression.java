package org.jelik.parser.ast.branching;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ThenKeyword;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IfExpression extends ExpressionReferencingType implements StackConsumer {

    @Getter
    private final IfKeyword ifKeyword;

    @Getter
    private IfConditionExpression conditionExpression;

    @Getter
    private final ThenKeyword thenKeyword;

    @Getter
    private BasicBlock basicBlock;

    @Getter
    private final EndKeyword endKeyword;

    public IfExpression(IfKeyword ifKeyword,
                        IfConditionExpression condition,
                        ThenKeyword thenKeyword,
                        BasicBlock block,
                        EndKeyword endKeyword) {

        super(new IfNodeContext());
        this.ifKeyword = ifKeyword;
        this.conditionExpression = condition;
        condition.setParent(this);
        this.thenKeyword = thenKeyword;
        this.basicBlock = block;
        block.setParent(this);
        this.endKeyword = endKeyword;
    }

    @Override
    public int getStartRow() {
        return ifKeyword.getRow();
    }

    @Override
    public Type getType() {
        return this.basicBlock.getType();
    }

    @Override
    public Type getGenericType() {
        return this.basicBlock.getGenericType();
    }

    @Override
    public Type getReturnType() {
        return basicBlock.getReturnType();
    }

    @Override
    public Type getGenericReturnType() {
        return basicBlock.getGenericReturnType();
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        if (oldNode == this.conditionExpression) {
            this.conditionExpression = ((IfConditionExpression) newNode);
            newNode.parent = this;
        } else if (oldNode == this.basicBlock) {
            this.basicBlock = ((BasicBlock) newNode);
            newNode.parent = this;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
