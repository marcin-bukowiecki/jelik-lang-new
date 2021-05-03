package org.jelik.parser.ast.branching;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ElifKeyword;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class ElifExpression extends ASTNodeImpl implements StackConsumer, IfExpression, ElseExpression {

    private final ElifKeyword elifKeyword;

    private final IfConditionExpressionImpl conditionExpression;

    private final Token leftCurl;

    private final BasicBlockImpl basicBlock;

    private final Token rightCurl;

    private ElseExpression elseExpression;

    private final IfNodeContext nodeContext;

    public ElifExpression(@NotNull ElifKeyword elifKeyword,
                          @NotNull IfConditionExpressionImpl ifConditionExpression,
                          @NotNull Token leftCurl,
                          @NotNull BasicBlockImpl block,
                          @Nullable Token rightCurl) {
        this.nodeContext = new IfNodeContext();
        this.elifKeyword = elifKeyword;
        this.conditionExpression = ifConditionExpression;
        this.conditionExpression.setParent(this);
        this.leftCurl = leftCurl;
        this.basicBlock = block;
        this.basicBlock.setParent(this);
        this.rightCurl = rightCurl;
    }

    public void setElseExpression(@NotNull ElseExpression elseExpression) {
        this.elseExpression = elseExpression;
        this.elseExpression.setParent(this);
    }

    public ElifKeyword getElifKeyword() {
        return elifKeyword;
    }

    public IfConditionExpressionImpl getConditionExpression() {
        return conditionExpression;
    }

    public BasicBlockImpl getBasicBlock() {
        return basicBlock;
    }

    public Optional<ElseExpression> getElseExpressionOpt() {
        return Optional.ofNullable(elseExpression);
    }

    @NotNull
    @Override
    public IfNodeContext getContext() {
        return this.nodeContext;
    }

    public @Nullable ElseExpression getElseExpression() {
        return this.elseExpression;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitElifExpression(this, compilationContext);
    }

    @Override
    public String toString() {
        return getElifKeyword() + " " + conditionExpression.toString() +
                leftCurl.toString() + basicBlock.toString() + rightCurl.toString() +
                (elseExpression != null ? elseExpression.toString() : "");
    }

    @Nullable
    @Override
    public LabelNode getFinishLabel() {
        return getElseExpression() == null ? getContext().getFinishLabel() : getElseExpression().getFinishLabel();
    }

    @Override
    public boolean endsWithReturnStmt() {
        return basicBlock.endsWithReturnStmt();
    }

    @Override
    public boolean isLast() {
        return elseExpression == null;
    }

    @Override
    public @NotNull Type getReturnType() {
        return getElseExpression() == null ? basicBlock.getReturnType() : getElseExpression().getReturnType();
    }

    @Override
    public @NotNull Type getGenericReturnType() {
        return getElseExpression() == null ? basicBlock.getGenericReturnType() : getElseExpression().getGenericReturnType();
    }

    @Override
    public Type getType() {
        return basicBlock.getType();
    }

    @Override
    public Type getGenericType() {
        return basicBlock.getGenericType();
    }
}
