package org.jelik.parser.ast.visitors.interfaces;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.functions.*;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.ast.visitors.functions.MethodDeclarationVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jelik.parser.token.keyword.MetKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class InterfaceMethodVisitor extends MethodDeclarationVisitor {

    public InterfaceMethodVisitor(@NotNull MetKeyword metKeyword) {
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
        if (functionReturn == null) {
            functionReturn = VoidFunctionReturn.INSTANCE;
        }
        return new InterfaceMethodDeclaration(
                funKeyword,
                name,
                parameterList,
                functionReturn,
                typeParameterListNode == null ? TypeParameterListNode.Companion.getEMPTY() : typeParameterListNode);
    }

    @Override
    public void visitFunKeyword(@NotNull FunKeyword funKeyword, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitRightCurl(@NotNull RightCurlToken rightCurlToken, @NotNull ParseContext parseContext) {

    }
}
