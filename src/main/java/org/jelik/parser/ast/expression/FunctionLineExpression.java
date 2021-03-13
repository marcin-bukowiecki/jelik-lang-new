package org.jelik.parser.ast.expression;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Expression corresponding to function code line. This can help to generate ASM line number
 *
 * @author Marcin Bukowiecki
 */
public class FunctionLineExpression extends ExpressionReferencingType {

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }
}
