package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.types.GenericTypeNode;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.token.CommaToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.QuestionMarkToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.AssignOperator;
import org.jelik.parser.token.operators.LesserOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Represents:
 *
 * T
 * []T
 * T?
 * T!!
 * T<F,R> etc.
 *
 * @author Marcin Bukowiecki
 */
public class TypeNodeVisitor implements ParseVisitor<TypeNode> {

    private final Token token;

    private TypeNode typeNode;

    public TypeNodeVisitor(Token token) {
        this.token = token;
    }

    @NotNull
    @Override
    public TypeNode visit(@NotNull ParseContext parseContext) {
        if (token.getTokenType() != ElementType.literal
                && token.getTokenType() != ElementType.leftBracket) {
            throw new SyntaxException("Expected parameter type declaration got '" + token.getText() + "'.",
                    token,
                    parseContext.getCurrentFilePath());
        }

        this.token.visit(this, parseContext);
        if (typeNode == null) {
            throw new SyntaxException("Unexpected token", token, parseContext.getCurrentFilePath());
        }
        return typeNode;
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        this.typeNode = new SingleTypeNode(literalToken);
        var peeked = parseContext.getLexer().peekNext();
        if (peeked.getTokenType() == ElementType.comma
                || peeked.getTokenType() == ElementType.assign
                || peeked.getTokenType() == ElementType.rightParenthesis
                || peeked.getTokenType() == ElementType.greaterOperator
                || peeked.getTokenType() == ElementType.leftCurl
                || peeked.getTokenType() == ElementType.colon
                || peeked.getTokenType() == ElementType.rightCurl) {
            return;
        }
        parseContext.getLexer().nextToken().visit(this, parseContext);
    }

    @Override
    public void visitQuestionMark(@NotNull QuestionMarkToken questionMarkToken, @NotNull ParseContext parseContext) {
        if (this.typeNode == null) {
            throw new SyntaxException("Unexpected token", questionMarkToken, parseContext.getCurrentFilePath());
        }
        this.typeNode.setQuestionMarkToken(questionMarkToken);
    }

    @Override
    public void visit(@NotNull LeftBracketToken leftBracketToken, @NotNull ParseContext parseContext) {
        this.typeNode = new ArrayTypeNodeVisitor(leftBracketToken).visit(parseContext);
    }

    @Override
    public void visit(@NotNull LesserOperator lesserOperator, @NotNull ParseContext parseContext) {
        this.typeNode = new GenericTypeNode(this.typeNode, new TypeParameterListVisitor(lesserOperator).visit(parseContext));
    }

    @Override
    public void visitRightCurl(@NotNull RightCurlToken rightCurlToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitAssign(AssignOperator assignOperator, ParseContext parseContext) {

    }

    @Override
    public void visitLeftCurl(@NotNull LeftCurlToken leftCurlToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitRightParenthesis(@NotNull RightParenthesisToken rightParenthesisToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull CommaToken commaToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(EofTok eofTok, ParseContext parseContext) {

    }
}
