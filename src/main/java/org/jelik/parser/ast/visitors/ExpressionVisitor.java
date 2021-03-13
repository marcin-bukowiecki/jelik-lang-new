package org.jelik.parser.ast.visitors;

import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.NullExpr;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.arguments.ArgumentListVisitor;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.expression.ParenthesisExpression;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.numbers.FalseNode;
import org.jelik.parser.ast.numbers.TrueNode;
import org.jelik.parser.ast.operators.NegExpr;
import org.jelik.parser.ast.strings.StringParser;
import org.jelik.parser.exceptions.SyntaxException;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.DotToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.FalseToken;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.NullToken;
import org.jelik.parser.token.RightBracketToken;
import org.jelik.parser.token.TrueToken;
import org.jelik.parser.token.operators.AbstractOperator;
import org.jelik.parser.token.operators.AndOperator;
import org.jelik.parser.token.operators.AsOperator;
import org.jelik.parser.token.operators.BitAndOperator;
import org.jelik.parser.token.operators.BitOrOperator;
import org.jelik.parser.token.operators.BitXorOperator;
import org.jelik.parser.token.operators.DivideOperator;
import org.jelik.parser.token.operators.GreaterOperator;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.NewLineToken;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jelik.parser.token.operators.AddOperator;
import org.jelik.parser.token.operators.AssignOperator;
import org.jelik.parser.token.operators.DecrOperator;
import org.jelik.parser.token.operators.EqualOperator;
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
    public void visit(EofTok eofTok, ParseContext parseContext) {

    }

    @Override
    public void visit(ApostropheToken apostropheToken, ParseContext parseContext) {
        var expr = new StringParser(apostropheToken).visit(parseContext);
        setAndGoFurther(expr, parseContext);
    }

    @Override
    public void visitRightParenthesis(RightParenthesisToken rightParenthesisToken, ParseContext parseContext) {

    }

    @Override
    public void visitReturnKeyword(@NotNull ReturnKeyword returnKeyword, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitLeftParenthesis(LeftParenthesisToken leftParenthesisToken, ParseContext parseContext) {
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
    public void visit(@NotNull  LesserOperator lesserOperator, @NotNull  ParseContext parseContext) {
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
    public void visitSubtract(SubtractOperator subtractOperator, ParseContext parseContext) {
        if (this.expression == null) {
            var expr = new NegExpr(subtractOperator, new ExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext));
            setAndGoFurther(expr, parseContext);
        } else {
            this.expression = new OpExpressionVisitor(this.expression, subtractOperator).visit(parseContext);
        }
    }

    @Override
    public void visitMul(MulOperator mulOperator, ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, mulOperator).visit(parseContext);
    }

    @Override
    public void visitDivide(DivideOperator divideOperator, ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, divideOperator).visit(parseContext);
    }

    @Override
    public void visitDot(DotToken dotToken, ParseContext parseContext) {
        var expr =  new DotCallExpr(dotToken);
        setAndGoFurther(expr, parseContext);
    }

    @Override
    public void visitNewLine(@NotNull NewLineToken newLineToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitRightCurl(RightCurlToken rightCurlToken, ParseContext parseContext) {

    }

    @Override
    public void visit(ElseKeyword elseKeyword, ParseContext parseContext) {

    }

    @Override
    public void visit(EndKeyword endKeyword, ParseContext parseContext) {

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
            setAndGoFurther(new ArrayOrMapGetExpr(leftBracketToken, expr, ((RightBracketToken) lexer.getCurrent())), parseContext);
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
            case rightCurl:
            case ifKeyword:
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
