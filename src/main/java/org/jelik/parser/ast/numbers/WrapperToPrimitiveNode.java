package org.jelik.parser.ast.numbers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.expression.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class WrapperToPrimitiveNode extends CastToNode {

    public WrapperToPrimitiveNode(Expression subject) {
        super(subject);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
