package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IntegerToInt32NodeWrapper extends CastToNode {

    public IntegerToInt32NodeWrapper(Expression subject) {
        super(subject);
        getNodeContext().setType(JVMIntType.INSTANCE);
        getNodeContext().setGenericType(JVMIntType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return getExpression().toString() + ".intValue()";
    }
}
