package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.TypedExpression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.jvm.JVMDoubleType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Float64Node extends TypedExpression {

    private final LiteralToken literalToken;

    private final double value;

    public Float64Node(LiteralToken literalToken, double value) {
        this.literalToken = literalToken;
        this.value = value;
        this.nodeContext.setType(JVMDoubleType.INSTANCE);
        this.nodeContext.setGenericType(JVMDoubleType.INSTANCE);
    }

    public double getValue() {
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
