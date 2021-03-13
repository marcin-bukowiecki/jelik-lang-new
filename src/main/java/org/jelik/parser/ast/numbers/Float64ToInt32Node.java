package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMIntType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Float64ToInt32Node extends CastToNode {

    public Float64ToInt32Node(Expression subject) {
        super(subject);
        this.nodeContext.setType(JVMIntType.INSTANCE);
        this.nodeContext.setGenericType(JVMIntType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
