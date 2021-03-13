package org.jelik.parser.ast.numbers;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.jvm.JVMByteType;
import org.jelik.types.jvm.JVMDoubleType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class Int32ToInt8Node extends CastToNode {

    public Int32ToInt8Node(Expression subject) {
        super(subject);
        this.nodeContext.setType(JVMByteType.INSTANCE);
        this.nodeContext.setGenericType(JVMByteType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
