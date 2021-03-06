package org.jelik.parser.ast.branching;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class IfExpressionImpl extends ASTNodeImpl implements StackConsumer, IfExpression {

    private final IfKeyword ifKeyword;

    private IfConditionExpressionWrapperImpl conditionExpression;

    private final Token leftCurlToken;

    private BasicBlockImpl basicBlock;

    private final Token rightCurlToken;

    private ElseExpression elseExpression;

    private final IfNodeContext nodeContext;

    public IfExpressionImpl(@NotNull IfKeyword ifKeyword,
                            @NotNull IfConditionExpressionWrapperImpl condition,
                            @NotNull LeftCurlToken leftCurlToken,
                            @NotNull BasicBlockImpl block,
                            @Nullable RightCurlToken rightCurlToken) {

        this.nodeContext = new IfNodeContext();
        this.ifKeyword = ifKeyword;
        this.conditionExpression = condition;
        condition.setParent(this);
        this.leftCurlToken = leftCurlToken;
        this.basicBlock = block;
        block.setParent(this);
        this.rightCurlToken = rightCurlToken;
    }

    public void setElseExpression(@NotNull ElseExpression elseExpression) {
        this.elseExpression = elseExpression;
        this.elseExpression.setParent(this);
    }

    public @NotNull IfNodeContext getContext() {
        return nodeContext;
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
    public int getStartOffset() {
        return ifKeyword.getStartOffset();
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
            this.conditionExpression = ((IfConditionExpressionWrapperImpl) newNode);
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

    public IfKeyword getIfKeyword() {
        return ifKeyword;
    }

    public IfConditionExpressionWrapperImpl getConditionExpression() {
        return conditionExpression;
    }

    public Token getLeftCurlToken() {
        return leftCurlToken;
    }

    public BasicBlockImpl getBasicBlock() {
        return basicBlock;
    }

    public Token getRightCurlToken() {
        return rightCurlToken;
    }

    public IfNodeContext getNodeContext() {
        return nodeContext;
    }
}
