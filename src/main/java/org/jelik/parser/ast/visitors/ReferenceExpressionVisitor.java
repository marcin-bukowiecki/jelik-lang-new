package org.jelik.parser.ast.visitors;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.token.DotToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ReferenceExpressionVisitor extends ExpressionVisitor {

    public ReferenceExpressionVisitor(Token start) {
        super(start);
    }

    @Override
    public @NotNull Expression visit(@NotNull ParseContext parseContext) {
        start.accept(this, parseContext);
        if (expression == null) {
            throw new SyntaxException("Unexpected token", start, parseContext);
        }
        return expression;
    }

    @Override
    public void visitDefaultValueOperator(@NotNull DefaultValueOperator defaultValueOperator,
                                          @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitNullSafeCallOperator(@NotNull NullSafeCallOperator nullSafeCallOperator,
                                          @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitDot(@NotNull DotToken dotToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitAnd(@NotNull AndOperator andOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitOr(@NotNull OrOperator orOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitEqual(@NotNull EqualOperator equalOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitNotEqual(NotEqualOperator notEqualOperator, ParseContext parseContext) {

    }

    @Override
    public void visitLesser(@NotNull LesserOperator lesserOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitGreater(@NotNull GreaterOperator greaterOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitSubtract(@NotNull SubtractOperator subtractOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitAdd(@NotNull AddOperator addOperator, @NotNull ParseContext parseContext) {

    }
}
