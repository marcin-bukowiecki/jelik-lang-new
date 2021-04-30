package org.jelik.parser.ast.visitors.functions;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.ast.visitors.TypeNodeVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionParameterVisitor implements TokenVisitor<FunctionParameter> {

    private final Token name;

    public FunctionParameterVisitor(Token name) {
        this.name = name;
    }

    @Override
    public @NotNull FunctionParameter visit(@NotNull ParseContext parseContext) {
        TypeNode typeNode = new TypeNodeVisitor(parseContext.getLexer().nextToken()).visit(parseContext);
        if (name.getTokenType() != ElementType.literal) {
            throw new SyntaxException("Expected parameter name.", name, parseContext.getCurrentFilePath());
        }
        return new FunctionParameter(typeNode, (LiteralToken) name,
                parseContext.getLexer().peekNext().getTokenType() == ElementType.comma ?
                        parseContext.getLexer().nextToken() : null);
    }
}
