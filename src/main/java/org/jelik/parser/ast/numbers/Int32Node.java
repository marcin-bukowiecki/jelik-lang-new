package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Int32Node extends TypedExpression {

    private final LiteralToken literalToken;

    private final int value;

    public Int32Node(LiteralToken literalToken, int value) {
        this.literalToken = literalToken;
        this.value = value;
        this.nodeContext.setType(JVMIntType.INSTANCE);
        this.nodeContext.setGenericType(JVMIntType.INSTANCE);
    }

    @Override
    public int getStartOffset() {
        return literalToken.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return literalToken.getEndOffset();
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return literalToken.toString();
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }
}
