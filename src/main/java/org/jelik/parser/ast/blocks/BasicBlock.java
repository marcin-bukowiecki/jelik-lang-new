package org.jelik.parser.ast.blocks;

import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface BasicBlock extends Expression {

    @NotNull List<@NotNull Expression> getExpressions();

    @NotNull BlockContext getBlockContext();

    @NotNull FunctionDeclaration getOwningFunction();

    void appendExpression(@NotNull Expression expression);

    void prependExpression(@NotNull Expression expression);

    @Nullable ASTNode getLast();
}
