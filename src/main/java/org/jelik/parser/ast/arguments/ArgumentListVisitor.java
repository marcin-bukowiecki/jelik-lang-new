package org.jelik.parser.ast.arguments;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author Marcin Bukowiecki
 */
public class ArgumentListVisitor implements ParseVisitor<ArgumentList> {

    private final LeftParenthesisToken leftParenthesisToken;

    public ArgumentListVisitor(LeftParenthesisToken leftParenthesisToken) {
        this.leftParenthesisToken = leftParenthesisToken;
    }

    @Override
    public @NotNull ArgumentList visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        ArrayList<Argument> arguments = new ArrayList<>();

        while (lexer.hasNextToken()) {
            var nextToken = lexer.nextToken();
            if (lexer.getCurrent().getTokenType() == ElementType.rightParenthesis) {
                break;
            }

            var argument = new ArgumentVisitor(nextToken).visit(parseContext);
            arguments.add(argument);

            if (lexer.getCurrent().getTokenType() == ElementType.rightParenthesis) {
                break;
            }
        }

        RightParenthesisToken rp = ((RightParenthesisToken) lexer.getCurrent());

        return new ArgumentList(leftParenthesisToken, arguments, rp);
    }
}
