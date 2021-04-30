package org.jelik.parser.ast.numbers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.TrueToken;
import org.jelik.types.JVMBooleanType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class TrueNode extends ExpressionWithType implements BooleanConstant {

    private final TrueToken literalToken;

    private boolean ignore = false;

    public TrueNode() {
        this(new TrueToken());
    }

    public TrueNode(boolean ignore) {
        this(new TrueToken());
        this.ignore = ignore;
    }

    public TrueNode(TrueToken literalToken) {
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
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "true";
    }
}
