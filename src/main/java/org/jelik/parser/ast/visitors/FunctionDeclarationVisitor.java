package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.ast.functions.FunctionReturn;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.functions.FunctionBody;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.operators.LesserOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionDeclarationVisitor implements ParseVisitor<FunctionDeclaration> {

    private final FunKeyword funKeyword;
    private FunctionParameterList parameterList;
    private FunctionReturn functionReturn;
    private FunctionBody functionBody;

    public FunctionDeclarationVisitor(FunKeyword funKeyword) {
        this.funKeyword = funKeyword;
    }

    public @NotNull FunctionDeclaration visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        Token name = lexer.nextToken();
        Token nextToken = lexer.nextToken();
        TypeParameterListNode typeParameterListNode = TypeParameterListNode.Companion.getEMPTY();;

        if (nextToken.getTokenType() == ElementType.leftParenthesis) {
            nextToken.visit(this, parseContext);
        } else if (nextToken.getTokenType() == ElementType.lesserOperator) {
            typeParameterListNode = new TypeParameterListVisitor(((LesserOperator) nextToken)).visit(parseContext);
            nextToken = lexer.nextToken();
            nextToken.visit(this, parseContext);
        } else {
            throw new SyntaxException("Unexpected token after function name. Expected parameter list or type variable.",
                    nextToken,
                    parseContext.getCurrentFilePath());
        }

        nextToken = lexer.nextToken();
        if (nextToken.getTokenType() == ElementType.arrow) {
            functionReturn = new FunctionReturn(nextToken,
                    new TypeNodeVisitor(parseContext.getLexer().nextToken()).visit(parseContext));
        }

        nextToken = lexer.nextToken();
        nextToken.visit(this, parseContext);

        return new FunctionDeclaration(funKeyword,
                ((LiteralToken) name),
                parameterList,
                functionReturn,
                functionBody,
                typeParameterListNode);
    }

    @Override
    public void visitLeftParenthesis(@NotNull LeftParenthesisToken leftParenthesisToken, @NotNull ParseContext parseContext) {
        this.parameterList = new FunctionParameterListVisitor(leftParenthesisToken).visit(parseContext);
    }

    @Override
    public void visitLeftCurl(@NotNull LeftCurlToken leftCurlToken, @NotNull ParseContext parseContext) {
        this.functionBody = new FunctionBodyVisitor(leftCurlToken).visit(parseContext);
    }
}
