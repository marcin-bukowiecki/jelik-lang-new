package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.EmptyExpression;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ExpressionWrapper;
import org.jelik.parser.ast.expression.StackConsumer;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ReturnExpr extends ExpressionWrapper implements ConsumingExpression, StackConsumer {

    private final Token returnKeyword;

    public ReturnExpr(@NotNull Token returnKeyword, @NotNull Expression expression) {
        super(expression);
        this.returnKeyword = returnKeyword;
    }

    public static ReturnExpr voidReturn() {
        return new ReturnExpr(ReturnKeyword.MOCK, EmptyExpression.INSTANCE);
    }

    @Override
    public int getStartOffset() {
        return returnKeyword.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        if (getExpression() == null) {
            return returnKeyword.getEndOffset();
        }
        return getExpression().getEndOffset();
    }

    public Token getReturnKeyword() {
        return returnKeyword;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitReturnExpr(this, compilationContext);
    }

    @Override
    public boolean isEmpty() {
        return getExpression() instanceof EmptyExpression;
    }

    @Override
    public String toString() {
        return getReturnKeyword() + " " + (getExpression() instanceof EmptyExpression ? "" : getExpression().toString());
    }
}
