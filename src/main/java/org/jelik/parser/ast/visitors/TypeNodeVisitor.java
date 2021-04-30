package org.jelik.parser.ast.visitors;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.types.CompositeTypeNode;
import org.jelik.parser.ast.types.FunctionTypeNode;
import org.jelik.parser.ast.types.GenericTypeNode;
import org.jelik.parser.ast.types.MaybeTypeNode;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.arrays.ArrayTypeNodeVisitor;
import org.jelik.parser.token.CommaToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.PipeToken;
import org.jelik.parser.token.QuestionMarkToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.AssignOperator;
import org.jelik.parser.token.operators.LesserOperator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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
public class TypeNodeVisitor implements TokenVisitor<TypeNode> {

    private static final Set<ElementType> parseBreakers = EnumSet.of(
            ElementType.comma,
            ElementType.literal,
            ElementType.assign,
            ElementType.rightParenthesis,
            ElementType.greaterOperator,
            ElementType.leftCurl,
            ElementType.colon,
            ElementType.pipe,
            ElementType.rightCurl
    );

    private final Token token;

    private final List<LiteralToken> paths = new ArrayList<>();

    private TypeNode typeNode;

    public TypeNodeVisitor(@NotNull Token token) {
        this.token = token;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    @Override
    public @NotNull TypeNode visit(@NotNull ParseContext parseContext) {
        TokenUtils.checkCurrentNotMatching(parseContext,
                "type.parameterDeclaration",
                ElementType.literal, ElementType.leftBracket, ElementType.pipe);
        this.token.accept(this, parseContext);
        if (typeNode == null) {
            throw new SyntaxException("Unexpected token", token, parseContext.getCurrentFilePath());
        }
        if (paths.isEmpty()) {
            return typeNode;
        } else {
            return new CompositeTypeNode(paths, typeNode);
        }
    }

    @Override
    public void visitPipe(@NotNull PipeToken pipeToken, @NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();
        final List<TypeNode> types = new ArrayList<>();
        if (lexer.peekNext().getTokenType() != ElementType.pipe) {
            while (lexer.hasNextToken()) {
                final TypeNode typeNode = new TypeNodeVisitor(lexer.nextToken()).visit(parseContext);
                types.add(typeNode);
                var next = lexer.nextToken();
                if (next.getTokenType() == ElementType.pipe) {
                    break;
                }
            }
        } else {
            lexer.nextToken();
        }
        var rightPipe = ((PipeToken) lexer.getCurrent());
        var arrow = lexer.nextToken();
        TokenUtils.checkCurrentNotMatching(parseContext, ElementType.arrow);
        var returnType =  new TypeNodeVisitor(lexer.nextToken()).visit(parseContext);
        this.typeNode = new FunctionTypeNode(pipeToken, types, rightPipe, arrow, returnType);
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        var peeked = parseContext.getLexer().peekNext();
        if (peeked.getTokenType() == ElementType.dot) {
            this.paths.add(literalToken);
            parseContext.getLexer().nextToken();
            parseContext.getLexer().nextToken().accept(this, parseContext);
            return;
        }

        this.typeNode = new SingleTypeNode(literalToken);

        if (parseBreakers.contains(peeked.getTokenType())) {
            return;
        }

        if (peeked.getTokenType() == ElementType.returnKeyword) {
            if (peeked.getEndRow() == this.typeNode.getEndRow()) {
                throw new SyntaxException("Unexpected token", peeked, parseContext.getCurrentFilePath());
            } else {
                return;
            }
        }

        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visitQuestionMark(@NotNull QuestionMarkToken questionMarkToken, @NotNull ParseContext parseContext) {
        if (this.typeNode == null) {
            throw new SyntaxException("Unexpected token", questionMarkToken, parseContext.getCurrentFilePath());
        }
        this.typeNode = new MaybeTypeNode(this.typeNode, questionMarkToken);
    }

    @Override
    public void visit(@NotNull LeftBracketToken leftBracketToken, @NotNull ParseContext parseContext) {
        if (this.typeNode != null) {
            throw new SyntaxException("Unexpected token", leftBracketToken, parseContext.getCurrentFilePath());
        }
        this.typeNode = new ArrayTypeNodeVisitor(leftBracketToken)
                .visit(parseContext);
    }

    @Override
    public void visitLesser(@NotNull LesserOperator lesserOperator, @NotNull ParseContext parseContext) {
        this.typeNode = new GenericTypeNode(this.typeNode, new TypeParameterListVisitor(lesserOperator)
                        .visit(parseContext));
    }

    @Override
    public void visitRightCurl(@NotNull RightCurlToken rightCurlToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitAssign(@NotNull AssignOperator assignOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitLeftCurl(@NotNull LeftCurlToken leftCurlToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitRightParenthesis(@NotNull RightParenthesisToken rightParenthesisToken,
                                      @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull CommaToken commaToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull EofTok eofTok, @NotNull ParseContext parseContext) {

    }
}
