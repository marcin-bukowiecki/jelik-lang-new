package org.jelik.parser.ast.branching;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Marcin Bukowiecki
 */
public class ElseExpressionImpl
        extends ASTNodeImpl
        implements ElseExpression, ConditionExpression<ElseNodeContext> {

    @Getter
    private final ElseKeyword elseKeyword;

    @Getter
    private final BasicBlockImpl basicBlock;

    @Getter
    private final Token rightCurlToken;

    private final ElseNodeContext nodeContext;

    public ElseExpressionImpl(ElseKeyword elseKeyword, BasicBlockImpl block, Token rightCurlToken) {
        this.nodeContext = new ElseNodeContext();
        this.elseKeyword = elseKeyword;
        this.basicBlock = block;
        this.basicBlock.setParent(this);
        this.rightCurlToken = rightCurlToken;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Nullable
    @Override
    public LabelNode getFinishLabel() {
        return getContext().getFinishLabel();
    }

    @NotNull
    @Override
    public ElseNodeContext getContext() {
        return this.nodeContext;
    }

    @Override
    public boolean isLast() {
        return true;
    }

    @Override
    public void setElseExpression(@NotNull ElseExpression elseExpression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Type getReturnType() {
        return basicBlock.getReturnType();
    }

    @Override
    public @NotNull Type getGenericReturnType() {
        return basicBlock.getGenericReturnType();
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
