package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

public class ConditionBlockVisitor extends BlockVisitor {

    @Override
    public @NotNull BasicBlock visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();

        while (lexer.hasNextToken() && lexer.peekNext().getTokenType() != ElementType.endKeyword && lexer.peekNext().getTokenType() != ElementType.elseKeyword) {
            Token nextToken = lexer.nextToken();
            nextToken.visit(this, parseContext);
        }

        lexer.nextToken();

        return new BasicBlock(expressionList);
    }
}
