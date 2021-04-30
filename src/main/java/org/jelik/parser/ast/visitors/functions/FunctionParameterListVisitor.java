package org.jelik.parser.ast.visitors.functions;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionParameterListVisitor implements TokenVisitor<FunctionParameterList> {

    private final LeftParenthesisToken start;

    public FunctionParameterListVisitor(LeftParenthesisToken start) {
        this.start = start;
    }

    @Override
    public @NotNull FunctionParameterList visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        List<FunctionParameter> parameterList = new ArrayList<>();

        while (lexer.hasNextToken()) {
            Token nextToken = lexer.nextToken();
            if (nextToken.getTokenType() == ElementType.rightParenthesis) {
                break;
            } else {
                parameterList.add(new FunctionParameterVisitor(nextToken).visit(parseContext));
            }

            if (lexer.getCurrent().getTokenType() == ElementType.rightParenthesis) {
                break;
            }
        }
        var end = (RightParenthesisToken) lexer.getCurrent();

        return new FunctionParameterList(start, parameterList, end);
    }
}
