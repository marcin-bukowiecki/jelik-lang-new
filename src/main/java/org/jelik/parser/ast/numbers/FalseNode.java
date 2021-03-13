package org.jelik.parser.ast.numbers;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.FalseToken;
import org.jelik.types.JVMBooleanType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FalseNode extends ExpressionWithType implements BooleanConstant {

    private final FalseToken literalToken;

    private boolean ignore = false;

    public FalseNode(FalseToken literalToken) {
        this.literalToken = literalToken;
        this.nodeContext.setType(JVMBooleanType.INSTANCE);
        this.nodeContext.setGenericType(JVMBooleanType.INSTANCE);
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isIgnore() {
        return ignore;
    }

    @Override
    public int getStartRow() {
        return literalToken.getRow();
    }

    @Override
    public int getEndRow() {
        return literalToken.getEndRow();
    }

    @Override
    public int getStartCol() {
        return literalToken.getCol();
    }

    @Override
    public int getEndCol() {
        return literalToken.getEndCol();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}