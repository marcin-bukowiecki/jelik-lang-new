package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.jvm.JVMFloatType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Float32Node extends TypedExpression {

    private final LiteralToken literalToken;

    private final float value;

    public Float32Node(LiteralToken literalToken, float value) {
        this.literalToken = literalToken;
        this.value = value;
        this.nodeContext.setType(JVMFloatType.INSTANCE);
        this.nodeContext.setGenericType(JVMFloatType.INSTANCE);
    }

    public float getValue() {
        return value;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return String.valueOf(literalToken);
    }
}
