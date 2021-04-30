package org.jelik.parser.ast.visitors.functions;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.functions.ExtensionFunctionDeclarationImpl;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.VoidFunctionReturn;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.ast.visitors.TypeNodeVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for extension function
 *
 * @author Marcin Bukowiecki
 */
public class ExtensionFunctionDeclarationVisitor extends FunctionDeclarationVisitor {

    private final Token extKeyword;

    public ExtensionFunctionDeclarationVisitor(@NotNull Token extKeyword, @NotNull Token funKeyword) {
        super(funKeyword);
        this.extKeyword = extKeyword;
    }

    @Override
    public @NotNull FunctionDeclaration visit(@NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();
        final TypeNode owner = new TypeNodeVisitor(lexer.nextToken()).visit(parseContext);
        final Token nextToken = lexer.nextToken();
        if (nextToken.getTokenType() != ElementType.literal) {
            throw new SyntaxException("Expected function name", nextToken, parseContext);
        }
        nextToken.accept(this, parseContext);
        return new ExtensionFunctionDeclarationImpl(extKeyword,
                owner,
                funKeyword,
                name,
                parameterList,
                functionReturn == null ? new VoidFunctionReturn() : functionReturn,
                functionBody,
                typeParameterListNode == null ? TypeParameterListNode.Companion.getEMPTY() : typeParameterListNode);
    }
}
