package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.FalseToken;
import org.jelik.types.JVMBooleanType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FalseNode extends TypedExpression implements BooleanConstant {

    private final FalseToken literalToken;

    private boolean ignore = false;

    public FalseNode(boolean ignore) {
        this(new FalseToken());
        this.ignore = ignore;
    }

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
    public int getStartOffset() {
        return literalToken.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return literalToken.getEndOffset();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "false";
    }
}
