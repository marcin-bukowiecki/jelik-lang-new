package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author Marcin Bukowiecki
 */
public class TypeParameterListVisitor implements ParseVisitor<TypeParameterListNode> {

    private final Token token;

    public TypeParameterListVisitor(Token token) {
        this.token = token;
    }

    @NotNull
    @Override
    public TypeParameterListNode visit(@NotNull ParseContext parseContext) {
        var lexer = parseContext.getLexer();
        var types = new ArrayList<>();
        var commas = new ArrayList<>();
        while (lexer.hasNextToken()) {
            final Token nextToken = lexer.nextToken();
            final TypeNode visit = new TypeNodeVisitor(nextToken).visit(parseContext);
            types.add(visit);

        }

        return TypeParameterListNode.Companion.getEMPTY();
    }
}
