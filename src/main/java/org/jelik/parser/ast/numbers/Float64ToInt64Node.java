package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.JVMLongType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Float64ToInt64Node extends CastToNode {

    public Float64ToInt64Node(Expression subject) {
        super(subject);
        this.nodeContext.setType(JVMLongType.INSTANCE);
        this.nodeContext.setGenericType(JVMLongType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
