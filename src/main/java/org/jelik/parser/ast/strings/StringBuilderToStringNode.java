package org.jelik.parser.ast.strings;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.numbers.CastToNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMStringType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class StringBuilderToStringNode extends CastToNode {

    public StringBuilderToStringNode(Expression expression) {
        super(expression);
        this.getNodeContext().setType(JVMStringType.INSTANCE);
        this.getNodeContext().setGenericType(JVMStringType.INSTANCE);
    }

    @Override
    public void setType(@NotNull Type type) {

    }

    @Override
    public void setGenericType(@NotNull Type type) {

    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return getExpression().toString() + ".toString()";
    }
}
