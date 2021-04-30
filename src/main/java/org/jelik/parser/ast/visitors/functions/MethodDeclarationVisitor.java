package org.jelik.parser.ast.visitors.functions;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.MethodDeclaration;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.MetKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class MethodDeclarationVisitor extends FunctionDeclarationVisitor {

    public MethodDeclarationVisitor(@NotNull MetKeyword metKeyword) {
        super(metKeyword);
    }

    @Override
    public @NotNull FunctionDeclaration visit(@NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();
        final Token nextToken = lexer.nextToken();
        if (nextToken.getTokenType() != ElementType.literal) {
            throw new SyntaxException("Expected method name", nextToken, parseContext);
        }
        nextToken.accept(this, parseContext);
        return new MethodDeclaration(funKeyword,
                name,
                parameterList,
                functionReturn,
                functionBody,
                typeParameterListNode == null ? TypeParameterListNode.Companion.getEMPTY() : typeParameterListNode);
    }
}
