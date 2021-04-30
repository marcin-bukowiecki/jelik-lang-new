package org.jelik.parser.ast.numbers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.jvm.JVMLongType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class Int64Node extends ExpressionWithType {

    private final LiteralToken literalToken;

    private final long value;

    public Int64Node(LiteralToken literalToken, long value) {
        this.literalToken = literalToken;
        this.value = value;
        this.nodeContext.setType(JVMLongType.INSTANCE);
        this.nodeContext.setGenericType(JVMLongType.INSTANCE);
    }

    public long getValue() {
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
}
