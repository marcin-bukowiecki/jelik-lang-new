package org.jelik.parser.ast.numbers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class CharToInt64Node extends CastToNode {

    public CharToInt64Node(Expression subject) {
        super(subject);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
