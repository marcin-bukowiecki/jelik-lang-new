package org.jelik.parser.ast.strings;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.numbers.CastToNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMStringType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class StringBuilderToStringNode extends CastToNode {

    public StringBuilderToStringNode(Expression subject) {
        super(subject);
        this.nodeContext.setType(JVMStringType.INSTANCE);
        this.nodeContext.setGenericType(JVMStringType.INSTANCE);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return getSubject().toString() + ".toString()";
    }
}
