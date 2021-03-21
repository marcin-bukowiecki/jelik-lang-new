package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.KeyValueExpr;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.MapCreateExpr;
import org.jelik.parser.ast.NullExpr;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.arguments.ArgumentListVisitor;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.expression.EmptyExpression;
import org.jelik.parser.ast.expression.ParenthesisExpression;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.numbers.FalseNode;
import org.jelik.parser.ast.numbers.TrueNode;
import org.jelik.parser.ast.operators.NegExpr;
import org.jelik.parser.ast.strings.StringParser;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.ast.utils.ASTUtils;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.ColonToken;
import org.jelik.parser.token.CommaToken;
import org.jelik.parser.token.DotToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.FalseToken;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.NullToken;
import org.jelik.parser.token.RightBracketToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.TrueToken;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jelik.parser.token.operators.AddOperator;
import org.jelik.parser.token.operators.AndOperator;
import org.jelik.parser.token.operators.AsOperator;
import org.jelik.parser.token.operators.AssignOperator;
import org.jelik.parser.token.operators.BitAndOperator;
import org.jelik.parser.token.operators.BitOrOperator;
import org.jelik.parser.token.operators.BitXorOperator;
import org.jelik.parser.token.operators.DecrOperator;
import org.jelik.parser.token.operators.DivideOperator;
import org.jelik.parser.token.operators.EqualOperator;
import org.jelik.parser.token.operators.GreaterOperator;
import org.jelik.parser.token.operators.GreaterOrEqualOperator;
import org.jelik.parser.token.operators.IncrOperator;
import org.jelik.parser.token.operators.IsOperator;
import org.jelik.parser.token.operators.LesserOperator;
import org.jelik.parser.token.operators.LesserOrEqualOperator;
import org.jelik.parser.token.operators.MulOperator;
import org.jelik.parser.token.operators.NotEqualOperator;
import org.jelik.parser.token.operators.OrOperator;
import org.jelik.parser.token.operators.RemOperator;
import org.jelik.parser.token.operators.SubtractOperator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author Marcin Bukowiecki
 */
public class ExpressionVisitor implements ParseVisitor<Expression> {

    protected Token start;

    protected Expression expression;

    protected Expression currentExpression;

    public ExpressionVisitor(Token start) {
        this.start = start;
    }

    public ExpressionVisitor(Expression expression, Expression currentExpression) {
        this.expression = expression;
        this.currentExpression = currentExpression;
    }

    @Override
    public @NotNull Expression visit(@NotNull ParseContext parseContext) {
        start.visit(this, parseContext);
        return expression;
    }

    @Override
    public void visitLeftCurl(@NotNull LeftCurlToken leftCurlToken, @NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();
        var expressions = new ArrayList<KeyValueExpr>();
        while (lexer.hasNextToken()) {
            final Expression key = new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
            final ColonToken colonToken = ((ColonToken) lexer.getCurrent());
            final Expression value = new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
            expressions.add(new KeyValueExpr(key, colonToken, value));
            if (lexer.peekNext() instanceof RightCurlToken) {
                lexer.nextToken();
                break;
            }
        }
        setAndGoFurther(new MapCreateExpr(leftCurlToken, expressions, ((RightCurlToken) lexer.getCurrent())), parseContext);
    }

    @Override
    public void visit(@NotNull ApostropheToken apostropheToken, @NotNull ParseContext parseContext) {
        var expr = new StringParser(apostropheToken).visit(parseContext);
        setAndGoFurther(expr, parseContext);
    }

    @Override
    public void visitLeftParenthesis(@NotNull LeftParenthesisToken leftParenthesisToken, @NotNull ParseContext parseContext) {
        var expr = new ArgumentListVisitor(leftParenthesisToken).visit(parseContext);
        if (this.currentExpression instanceof LiteralExpr) {
            if (this.currentExpression.getParent() == null) {
                this.currentExpression = new FunctionCallExpr((LiteralExpr) this.currentExpression, expr);
                this.expression = null;
                setAndGoFurther(this.currentExpression, parseContext);
            } else {
                ASTNode parent = this.currentExpression.parent;
                LiteralExpr literalExpr = ((LiteralExpr) this.currentExpression);
                this.currentExpression = ((Expression) parent);
                FunctionCallExpr functionCallExpr = new FunctionCallExpr(literalExpr, expr);
                setAndGoFurther(functionCallExpr, parseContext);
            }
        } else {
            var newExpr = new ParenthesisExpression(expr.getLeftParenthesisToken(),
                    expr.getArguments().get(0).getExpression(),
                    expr.getRightParenthesisToken());
            setAndGoFurther(newExpr, parseContext);
        }
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        if (this.currentExpression != null && !(this.currentExpression instanceof DotCallExpr)) {
            throw new SyntaxException("Unexpected token", literalToken, parseContext.getCurrentFilePath());
        }
        var expr =  new LiteralExpr(literalToken);
        setAndGoFurther(expr, parseContext);
    }

    @Override
    public void visitAs(@NotNull AsOperator asOperator, @NotNull ParseContext parseContext) {
        checkLeftExpression(asOperator, parseContext);
        this.expression = new OpExpressionVisitor(this.expression, asOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull GreaterOperator greaterOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, greaterOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull BitAndOperator bitAndOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, bitAndOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull BitOrOperator bitOrOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, bitOrOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull LesserOperator lesserOperator, @NotNull  ParseContext parseContext) {
        try {
            if (this.currentExpression instanceof LiteralExpr) {
                final TypeParameterListNode visit = new TypeParameterListVisitor(lesserOperator).visit(parseContext);
                ((LiteralExpr) this.currentExpression).setTypeParameterListNode(visit);
                parseContext.getLexer().nextToken().visit(this, parseContext);
                return;
            }
        } catch (Exception ignored) {

        }
        parseContext.getLexer().recover(lesserOperator);
        this.expression = new OpExpressionVisitor(this.expression, lesserOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull OrOperator orOperator, @NotNull  ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, orOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull AndOperator andOperator, @NotNull  ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, andOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull BitXorOperator bitXorOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, bitXorOperator).visit(parseContext);
    }

    @Override
    public void visitAdd(@NotNull AddOperator addOperator, @NotNull  ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, addOperator).visit(parseContext);
    }


    @Override
    public void visitRem(@NotNull RemOperator remOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, remOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull EqualOperator equalOperator, @NotNull  ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, equalOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull LesserOrEqualOperator lesserOrEqualOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, lesserOrEqualOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull GreaterOrEqualOperator greaterOrEqualOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, greaterOrEqualOperator).visit(parseContext);
    }

    @Override
    public void visit(NotEqualOperator notEqualOperator, ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, notEqualOperator).visit(parseContext);
    }

    @Override
    public void visitAssign(AssignOperator assignOperator, ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, assignOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull IsOperator isOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, isOperator).visit(parseContext);
    }

    @Override
    public void visitSubtract(@NotNull SubtractOperator subtractOperator, @NotNull ParseContext parseContext) {
        if (this.expression == null) {
            var expr = new NegExpr(subtractOperator, new ExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext));
            setAndGoFurther(expr, parseContext);
        } else {
            this.expression = new OpExpressionVisitor(this.expression, subtractOperator).visit(parseContext);
        }
    }

    @Override
    public void visitMul(@NotNull MulOperator mulOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, mulOperator).visit(parseContext);
    }

    @Override
    public void visitDivide(@NotNull DivideOperator divideOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, divideOperator).visit(parseContext);
    }

    @Override
    public void visitDot(@NotNull DotToken dotToken, @NotNull ParseContext parseContext) {
        this.expression = new DotCallExpr(this.expression == null ? EmptyExpression.INSTANCE : this.expression, dotToken);
        this.currentExpression = this.expression;
        parseContext.getLexer().nextToken().visit(this, parseContext);
    }

    @Override
    public void visit(@NotNull DecrOperator decrOperator, @NotNull ParseContext parseContext) {
        var expr = new RightHandExpressionVisitor(decrOperator).visit(parseContext);
        setAndGoFurther(expr, parseContext);
    }

    @Override
    public void visit(@NotNull IncrOperator incrOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, incrOperator).visit(parseContext);
    }

    @Override
    public void visit(@NotNull LeftBracketToken leftBracketToken, @NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        if (this.expression != null) {
            Expression expr = new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
            ArrayOrMapGetExpr arrayOrMapGetExpr = new ArrayOrMapGetExpr(this.currentExpression,
                    leftBracketToken,
                    expr,
                    ((RightBracketToken) lexer.getCurrent()));
            this.currentExpression = null;
            this.expression = null;
            setAndGoFurther(arrayOrMapGetExpr, parseContext);
            return;
        }
        var args = new ArrayList<Expression>();
        while (lexer.hasNextToken()) {
            Expression visit = new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
            args.add(visit);
            if (lexer.getCurrent().getTokenType() == ElementType.rightBracket) {
                break;
            }
        }
        var right = (RightBracketToken) lexer.getCurrent();
        if (lexer.peekNext().canBeType()) {
            lexer.nextToken();
            var expr = new TypedArrayCreateExpr(leftBracketToken, args, right, new TypeNodeVisitor(lexer.nextToken()).visit(parseContext));
            setAndGoFurther(expr, parseContext);
        } else {
            var expr = new ArrayCreateExpr(leftBracketToken, args, right);
            setAndGoFurther(expr, parseContext);
        }
    }

    @Override
    public void visit(@NotNull NullToken nullToken, @NotNull ParseContext parseContext) {
        setAndGoFurther(new NullExpr(nullToken), parseContext);
    }

    @Override
    public void visit(@NotNull TrueToken trueToken, @NotNull ParseContext parseContext) {
        this.expression = new TrueNode(trueToken);
    }

    @Override
    public void visit(@NotNull FalseToken falseToken, @NotNull ParseContext parseContext) {
        this.expression = new FalseNode(falseToken);
    }

    @Override
    public void visit(EofTok eofTok, ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull CommaToken commaToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull RightBracketToken rightBracketToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitRightParenthesis(@NotNull RightParenthesisToken rightParenthesisToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitColon(@NotNull ColonToken colonToken, @NotNull ParseContext parseContext) {

    }

    private void checkLeftExpression(@NotNull AbstractOperator op, @NotNull ParseContext parseContext) {
        if (this.expression == null) {
            throw new SyntaxException("Expected left expression for " + op.getText() + " operator", op, parseContext.getCurrentFilePath());
        }
    }

    private void setAndGoFurther(@NotNull Expression expr, @NotNull ParseContext parseContext) {
        if (this.expression == null) {
            this.expression = expr;
        } else {
            this.currentExpression.setFurtherExpression(expr);
        }

        this.currentExpression = expr;

        if (this.currentExpression instanceof DotCallExpr) {
            parseContext.getLexer().nextToken().visit(this, parseContext);
            return;
        }

        Token peekedNext = parseContext.getLexer().peekNext();

        switch (peekedNext.getTokenType()) {
            case returnKeyword:
            case valKeyword:
            case varKeyword:
            case ifKeyword:
                ASTUtils.checkNewLine(this.currentExpression, peekedNext, parseContext);
            case rightCurl:
            case elseKeyword:
            case elifKeyword:
            case thenKeyword:
            case endKeyword:
                return;
        }

        if (peekedNext.getRow() != this.currentExpression.getEndRow()) {
            return;
        }

        parseContext.getLexer().nextToken().visit(this, parseContext);
    }
}
