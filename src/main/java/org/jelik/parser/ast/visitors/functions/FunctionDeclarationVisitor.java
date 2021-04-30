package org.jelik.parser.ast.visitors.functions;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.functions.FunctionBody;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.ast.functions.FunctionReturn;
import org.jelik.parser.ast.functions.VoidFunctionReturn;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.TypeNodeVisitor;
import org.jelik.parser.ast.visitors.TypeParameterListVisitor;
import org.jelik.parser.token.ArrowToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.LesserOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionDeclarationVisitor implements TokenVisitor<FunctionDeclaration> {

    protected final Token funKeyword;
    protected FunctionParameterList parameterList;
    protected TypeVariableListNode typeParameterListNode;
    protected FunctionReturn functionReturn;
    protected FunctionBody functionBody;
    protected LiteralToken name;

    public FunctionDeclarationVisitor(@NotNull Token funKeyword) {
        this.funKeyword = funKeyword;
    }

    public @NotNull FunctionDeclaration visit(@NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();
        final Token nextToken = lexer.nextToken();
        if (nextToken.getTokenType() != ElementType.literal) {
            throw new SyntaxException("Expected function name", nextToken, parseContext);
        }
        nextToken.accept(this, parseContext);
        return new FunctionDeclaration(funKeyword,
                name,
                parameterList,
                functionReturn == null ? new VoidFunctionReturn() : functionReturn,
                functionBody,
                typeParameterListNode == null ? TypeParameterListNode.Companion.getEMPTY() : typeParameterListNode);
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        if (name != null) {
            throw new SyntaxException("Unexpected token",
                    literalToken,
                    parseContext.getCurrentFilePath());
        }
        this.name = literalToken;
        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visitLesser(@NotNull LesserOperator lesserOperator, @NotNull ParseContext parseContext) {
        this.typeParameterListNode = new TypeParameterListVisitor(lesserOperator).visit(parseContext);
        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visitLeftParenthesis(@NotNull LeftParenthesisToken leftParenthesisToken,
                                     @NotNull ParseContext parseContext) {
        this.parameterList = new FunctionParameterListVisitor(leftParenthesisToken).visit(parseContext);
        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visitArrowToken(@NotNull ArrowToken arrowToken, @NotNull ParseContext parseContext) {
        functionReturn = new FunctionReturn(arrowToken,
                new TypeNodeVisitor(parseContext.getLexer().nextToken()).visit(parseContext));
        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visitLeftCurl(@NotNull LeftCurlToken leftCurlToken, @NotNull ParseContext parseContext) {
        if (parameterList == null) {
            throw new SyntaxException("Unexpected token after function name. Expected parameter list or type variable",
                    leftCurlToken,
                    parseContext.getCurrentFilePath());
        }
        this.functionBody = new FunctionBodyVisitor(leftCurlToken).visit(parseContext);
    }
}
