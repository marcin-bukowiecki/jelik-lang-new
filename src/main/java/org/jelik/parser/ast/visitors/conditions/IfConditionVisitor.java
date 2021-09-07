package org.jelik.parser.ast.visitors.conditions;

import org.jelik.compiler.helper.CompilerHelper;
import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.branching.IfConditionExpressionWrapperImpl;
import org.jelik.parser.ast.visitors.ExpressionVisitor;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Parses if condition if expr then ....
 *
 * @author Marcin Bukowiecki
 */
public class IfConditionVisitor implements TokenVisitor<IfConditionExpressionWrapperImpl> {

    private final Token current;

    public IfConditionVisitor(Token current) {
        this.current = current;
    }

    @Override
    public @NotNull IfConditionExpressionWrapperImpl visit(@NotNull ParseContext parseContext) {
        var condition = new IfConditionExpressionWrapperImpl(new ExpressionVisitor(current).visit(parseContext));
        parseContext.getLexer().nextToken();
        if (TokenUtils.currentNotMatching(parseContext, LeftCurlToken.class)) {
            parseContext.getLexer().nextToken();
            CompilerHelper.INSTANCE.raiseSyntaxError("leftCurl.expected", parseContext);
        }
        return condition;
    }
}
