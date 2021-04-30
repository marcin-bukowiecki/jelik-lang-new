package org.jelik.parser.ast.operators;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.ColonToken;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a:b
 *
 * @author Marcin Bukowiecki
 */
@Getter
public class SliceExpr extends AbstractOpExpr {

    public SliceExpr(Expression left, ColonToken colonToken, Expression right) {
        super(left, colonToken, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitSliceExpr(this, compilationContext);
    }

    @Override
    public String toString() {
        return left.toString() + ":" + right.toString();
    }
}
