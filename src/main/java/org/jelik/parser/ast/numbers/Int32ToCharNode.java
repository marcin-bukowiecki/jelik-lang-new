package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.JVMCharType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Int32ToCharNode extends CastToNode {

    public Int32ToCharNode(Expression subject) {
        super(subject);
        this.nodeContext.setType(JVMCharType.INSTANCE);
        this.nodeContext.setGenericType(JVMCharType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
