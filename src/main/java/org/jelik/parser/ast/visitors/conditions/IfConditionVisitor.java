package org.jelik.parser.ast.visitors.conditions;

import org.jelik.compiler.helper.CompilerHelper;
import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.branching.IfConditionExpressionImpl;
import org.jelik.parser.ast.visitors.ExpressionVisitor;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ThenKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * Parses if condition if expr then ....
 *
 * @author Marcin Bukowiecki
 */
public class IfConditionVisitor implements TokenVisitor<IfConditionExpressionImpl> {

    private final Token current;

    public IfConditionVisitor(Token current) {
        this.current = current;
    }

    @Override
    public @NotNull IfConditionExpressionImpl visit(@NotNull ParseContext parseContext) {
        var condition = new IfConditionExpressionImpl(new ExpressionVisitor(current).visit(parseContext));
        if (TokenUtils.nextNotMatching(parseContext, ThenKeyword.class)) {
            parseContext.getLexer().nextToken();
            CompilerHelper.INSTANCE.raiseSyntaxError("keyword.then.expected", parseContext);
        }
        return condition;
    }
}
