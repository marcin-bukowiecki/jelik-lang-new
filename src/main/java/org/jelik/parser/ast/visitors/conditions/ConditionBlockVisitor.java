package org.jelik.parser.ast.visitors.conditions;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.visitors.blocks.BlockVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ConditionBlockVisitor extends BlockVisitor {

    public ConditionBlockVisitor(Token start) {
        super(start);
    }

    @Override
    public @NotNull BasicBlockImpl visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();

        while (lexer.hasNextToken() &&
                lexer.peekNext().getTokenType() != ElementType.endKeyword &&
                lexer.peekNext().getTokenType() != ElementType.elifKeyword &&
                lexer.peekNext().getTokenType() != ElementType.elseKeyword) {

            Token nextToken = lexer.nextToken();
            nextToken.accept(this, parseContext);
        }

        lexer.nextToken();

        return new BasicBlockImpl(expressionList);
    }
}
