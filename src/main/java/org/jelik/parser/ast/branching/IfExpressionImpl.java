package org.jelik.parser.ast.branching;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ThenKeyword;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class IfExpressionImpl extends ASTNodeImpl implements StackConsumer, IfExpression {

    @Getter
    private final IfKeyword ifKeyword;

    @Getter
    private IfConditionExpressionImpl conditionExpression;

    @Getter
    private final ThenKeyword thenKeyword;

    @Getter
    private BasicBlockImpl basicBlock;

    @Getter
    private final EndKeyword endKeyword;

    private ElseExpression elseExpression;

    private final IfNodeContext nodeContext;

    public IfExpressionImpl(@NotNull IfKeyword ifKeyword,
                            @NotNull IfConditionExpressionImpl condition,
                            @NotNull ThenKeyword thenKeyword,
                            @NotNull BasicBlockImpl block,
                            @Nullable EndKeyword endKeyword) {

        this.nodeContext = new IfNodeContext();
        this.ifKeyword = ifKeyword;
        this.conditionExpression = condition;
        condition.setParent(this);
        this.thenKeyword = thenKeyword;
        this.basicBlock = block;
        block.setParent(this);
        this.endKeyword = endKeyword;
    }

    public void setElseExpression(@NotNull ElseExpression elseExpression) {
        this.elseExpression = elseExpression;
        this.elseExpression.setParent(this);
    }

    public @NotNull IfNodeContext getContext() {
        return ((IfNodeContext) nodeContext);
    }

    public @Nullable ElseExpression getElseExpression() {
        return elseExpression;
    }

    public Optional<ElseExpression> getElseExpressionOpt() {
        return Optional.ofNullable(elseExpression);
    }

    public @Nullable LabelNode getFinishLabel() {
        if (getElseExpression() == null) {
            return getContext().getFinishLabel();
        } else {
            return this.getElseExpression().getFinishLabel();
        }
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
            this.conditionExpression = ((IfConditionExpressionImpl) newNode);
            newNode.setParent(this);
        } else if (oldNode == this.basicBlock) {
            this.basicBlock = ((BasicBlockImpl) newNode);
            newNode.setParent(this);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public boolean endsWithReturnStmt() {
        return this.basicBlock.endsWithReturnStmt();
    }

    @Override
    public boolean isLast() {
        return elseExpression == null;
    }
}
