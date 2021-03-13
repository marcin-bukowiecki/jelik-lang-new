package org.jelik.parser.ast.visitors;

import com.google.common.collect.Lists;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.GenericTypeNode;
import org.jelik.parser.exceptions.SyntaxException;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class TypeNodeVisitor implements ParseVisitor<TypeNode> {

    private final Token token;

    public TypeNodeVisitor(Token token) {
        this.token = token;
    }

    @NotNull
    @Override
    public TypeNode visit(@NotNull ParseContext parseContext) {
        if (token.getTokenType() != ElementType.literal && token.getTokenType() != ElementType.leftBracket) {
            throw new SyntaxException("Expected parameter type declaration got '" + token.getText() + "'.",
                    token,
                    parseContext.getCurrentFilePath());
        }

        Lexer lexer = parseContext.getLexer();
        Token peeked = lexer.peekNext();

        if (peeked.getTokenType() == ElementType.comma ||
                peeked.getTokenType() == ElementType.rightParenthesis ||
                peeked.getTokenType() == ElementType.leftCurl || peeked.getTokenType() == ElementType.rightCurl) {

            return new SingleTypeNode(((LiteralToken) token));
        }
        if (peeked.getTokenType() == ElementType.literal) {
            return new SingleTypeNode(((LiteralToken) token));
        }
        if (peeked.getTokenType() == ElementType.greaterOperator) {
            return new SingleTypeNode(((LiteralToken) token));
        }
        if (peeked.getTokenType() == ElementType.rightBracket) {
            return new ArrayTypeNodeVisitor(((LeftBracketToken) token)).visit(parseContext);
        }
        if (peeked.getTokenType() == ElementType.lesserOperator) {
            var lesser = lexer.nextToken();
            var acc = Lists.<TypeNode>newArrayList();

            while (lexer.hasNextToken()) {
                var t = new TypeNodeVisitor(lexer.nextToken()).visit(parseContext);
                acc.add(t);
                if (lexer.peekNext().getTokenType() == ElementType.greaterOperator) {
                    lexer.nextToken();
                    break;
                }
            }

            var greater = lexer.getCurrent();

            return new GenericTypeNode(new SingleTypeNode(((LiteralToken) token)), acc);
        }

        throw new SyntaxException("Unexpected token", peeked, parseContext.getCurrentFilePath());
    }
}
