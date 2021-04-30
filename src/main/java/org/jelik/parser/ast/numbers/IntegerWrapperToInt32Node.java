package org.jelik.parser.ast.numbers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IntegerWrapperToInt32Node extends CastToNode {

    public IntegerWrapperToInt32Node(Expression subject) {
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
