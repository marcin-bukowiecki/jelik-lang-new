package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.types.CovariantTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.ast.types.WildCardTypeNode;
import org.jelik.parser.token.ColonToken;
import org.jelik.parser.token.CommaToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.GreaterOperator;
import org.jelik.parser.token.operators.LesserOperator;
import org.jelik.parser.token.operators.MulOperator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse <T, R, B>
 *
 * @author Marcin Bukowiecki
 */
public class TypeParameterListVisitor implements ParseVisitor<TypeParameterListNode> {

    private final LesserOperator token;

    private final List<TypeNode> types = new ArrayList<>();

    private final List<CommaToken> commas = new ArrayList<>();

    public TypeParameterListVisitor(@NotNull LesserOperator token) {
        this.token = token;
    }

    @NotNull
    @Override
    public TypeParameterListNode visit(@NotNull ParseContext parseContext) {
        var lexer = parseContext.getLexer();

        while (lexer.hasNextToken()) {
            final Token nextToken = lexer.nextToken();
            nextToken.visit(this, parseContext);
            if (lexer.getCurrent().getTokenType() == ElementType.comma) {
                commas.add(((CommaToken) lexer.getCurrent()));
            }
            if (lexer.getCurrent().getTokenType() == ElementType.greaterOperator) {
                break;
            }
        }
        return new TypeParameterListNode(this.token, types, commas, ((GreaterOperator) lexer.getCurrent()));
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        final TypeNode currentTypeNode = new TypeNodeVisitor(literalToken).visit(parseContext);
        final Token next = parseContext.getLexer().nextToken();
        if (next.getTokenType() == ElementType.colon) {
            var colon = ((ColonToken) next);
            final TypeNode nextTypeNode = new TypeNodeVisitor(parseContext.getLexer().nextToken()).visit(parseContext);
            types.add(new CovariantTypeNode(currentTypeNode, colon, nextTypeNode));
            parseContext.getLexer().nextToken();
        } else {
            types.add(currentTypeNode);
        }
    }

    @Override
    public void visitMul(@NotNull MulOperator op, @NotNull ParseContext parseContext) {
        types.add(new WildCardTypeNode(op));
        parseContext.getLexer().nextToken();
    }
}
