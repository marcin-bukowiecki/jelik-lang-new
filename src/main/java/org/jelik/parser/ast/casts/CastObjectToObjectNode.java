package org.jelik.parser.ast.casts;

import lombok.Getter;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.expression.EmptyExpression;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class CastObjectToObjectNode extends ASTNodeImpl implements Expression {

    private final Expression expression;

    private final Type from;

    private final Type to;

    public CastObjectToObjectNode(@NotNull Expression expression, Type from, Type to) {
        this.expression = expression;
        this.expression.setParent(this);
        this.from = from;
        this.to = to;
    }

    public CastObjectToObjectNode(Type from, Type to) {
        this(new EmptyExpression(), from, to);
    }

    @Override
    public Type getReturnType() {
        return to;
    }

    @Override
    public Type getGenericReturnType() {
        return to;
    }

    @Override
    public Type getType() {
        return to;
    }

    @Override
    public Type getGenericType() {
        return to;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return "((" + to.toString() + ") " + getExpression().toString() + ")";
    }
}
