package org.jelik.parser.ast.numbers;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class IntegerWrapperToInt32Node extends CastToNode {

    public IntegerWrapperToInt32Node(Expression subject, Expression further) {
        super(subject, further);
        this.nodeContext.setType(JVMIntType.INSTANCE);
        this.nodeContext.setGenericType(JVMIntType.INSTANCE);
    }

    public IntegerWrapperToInt32Node(Expression subject) {
        super(subject, null);
        this.nodeContext.setType(JVMIntType.INSTANCE);
        this.nodeContext.setGenericType(JVMIntType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return getSubject().toString() + ".intValue()" + (furtherExpression != null ? "." + furtherExpression.toString() : "");
    }
}
