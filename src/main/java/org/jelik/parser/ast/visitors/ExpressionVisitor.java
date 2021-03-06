package org.jelik.parser.ast.visitors;

import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.*;
import org.jelik.parser.ast.branching.BreakExprImpl;
import org.jelik.parser.ast.branching.ContinueExprImpl;
import org.jelik.parser.ast.branching.WhileConditionExpressionWrapperImpl;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.arguments.ArgumentListVisitor;
import org.jelik.parser.ast.arrays.ArrayCreateExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateExpr;
import org.jelik.parser.ast.arrays.TypedArrayCreateWithSizeExprTyped;
import org.jelik.parser.ast.expression.EmptyExpression;
import org.jelik.parser.ast.expression.ParenthesisExpressionWrapper;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.LambdaDeclarationExpression;
import org.jelik.parser.ast.loops.ForEachLoop;
import org.jelik.parser.ast.loops.LoopVar;
import org.jelik.parser.ast.loops.WhileLoopImpl;
import org.jelik.parser.ast.nullsafe.NullSafeCallExprImpl;
import org.jelik.parser.ast.numbers.FalseNode;
import org.jelik.parser.ast.numbers.TrueNode;
import org.jelik.parser.ast.operators.InExpr;
import org.jelik.parser.ast.operators.NegExpr;
import org.jelik.parser.ast.operators.NotExpr;
import org.jelik.parser.ast.operators.SliceExpr;
import org.jelik.parser.ast.strings.CharTypedExpression;
import org.jelik.parser.ast.strings.StringParser;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.utils.ASTUtils;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.ast.visitors.blocks.BlockVisitor;
import org.jelik.parser.ast.visitors.functions.LambdaDeclarationVisitor;
import org.jelik.parser.token.*;
import org.jelik.parser.token.keyword.BreakKeyword;
import org.jelik.parser.token.keyword.ContinueKeyword;
import org.jelik.parser.token.keyword.FalseToken;
import org.jelik.parser.token.keyword.TrueToken;
import org.jelik.parser.token.keyword.ForKeyword;
import org.jelik.parser.token.keyword.InKeyword;
import org.jelik.parser.token.keyword.LamKeyword;
import org.jelik.parser.token.keyword.WhileKeyword;
import org.jelik.parser.token.operators.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author Marcin Bukowiecki
 */
public class ExpressionVisitor implements TokenVisitor<Expression> {

    protected Token start;

    protected Expression expression;

    public ExpressionVisitor(Token start) {
        this.start = start;
    }

    @Override
    public @NotNull Expression visit(@NotNull ParseContext parseContext) {
        start.accept(this, parseContext);
        if (expression == null) {
            throw new SyntaxException("Unexpected token", start, parseContext);
        }
        return expression;
    }

    @Override
    public void visitQuestionMark(@NotNull QuestionMarkToken questionMarkToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitSingleApostropheToken(@NotNull SingleApostropheToken singleApostropheToken,
                                           @NotNull ParseContext parseContext) {
        var lexer = parseContext.getLexer();
        var tokens = new ArrayList<Token>();
        while (lexer.hasNextToken()) {
            var next = lexer.nextToken();
            tokens.add(next);
            if (next instanceof SingleApostropheToken) {
                break;
            }
        }
        this.expression = new CharTypedExpression(singleApostropheToken, tokens, ((SingleApostropheToken) lexer.getCurrent()));
        goFurther(parseContext);
    }

    @Override
    public void visitLamKeyword(@NotNull LamKeyword lamKeyword, @NotNull ParseContext parseContext) {
        this.expression = new LambdaDeclarationExpression(new LambdaDeclarationVisitor(lamKeyword).visit(parseContext));
        goFurther(parseContext);
    }

    @Override
    public void visitLeftCurl(@NotNull LeftCurlToken leftCurlToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull ApostropheToken apostropheToken, @NotNull ParseContext parseContext) {
        this.expression = new StringParser(apostropheToken).visit(parseContext);
        goFurther(parseContext);
    }

    @Override
    public void visitLeftParenthesis(@NotNull LeftParenthesisToken leftParenthesisToken,
                                     @NotNull ParseContext parseContext) {
        var expr = new ArgumentListVisitor(leftParenthesisToken).visit(parseContext);
        if (this.expression instanceof LiteralExpr) {
            LiteralExpr literalExpr = ((LiteralExpr) this.expression);
            this.expression = new FunctionCallExpr(literalExpr, expr);
        } else {
            this.expression = new ParenthesisExpressionWrapper(expr.getLeftParenthesisToken(),
                    expr.getArguments().get(0).getExpression(),
                    expr.getRightParenthesisToken());
        }
        goFurther(parseContext);
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();
        if (literalToken.getText().equals("mapOf") && lexer.peekNext() instanceof LeftCurlToken) {
            var leftCurlToken = ((LeftCurlToken) lexer.nextToken());
            var expressions = new ArrayList<KeyValueExpr>();
            if (!(lexer.peekNext() instanceof RightCurlToken)) {
                while (lexer.hasNextToken()) {
                    final SliceExpr key = (SliceExpr) new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
                    final ColonToken colonToken = (ColonToken) key.getOp();
                    final Expression value = key.getRight();
                    expressions.add(new KeyValueExpr(key.getLeft(), colonToken, value));
                    if (lexer.peekNext() instanceof RightCurlToken) {
                        lexer.nextToken();
                        break;
                    }
                }
            } else {
                lexer.nextToken();
            }
            this.expression = new MapCreateExpr(
                    leftCurlToken,
                    expressions,
                    ((RightCurlToken) lexer.getCurrent()));
        } else {
            this.expression =  new LiteralExpr(literalToken);
        }
        goFurther(parseContext);
    }

    @Override
    public void visitAs(@NotNull AsOperator asOperator, @NotNull ParseContext parseContext) {
        checkLeftExpression(asOperator, parseContext);
        this.expression = new OpExpressionVisitor(this.expression, asOperator).visit(parseContext);
    }

    @Override
    public void visitGreater(@NotNull GreaterOperator greaterOperator, @NotNull ParseContext parseContext) {
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
    public void visitBitSignedShiftLeftOperator(@NotNull BitSignedShiftLeftOperator bitShl,
                                                @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, bitShl).visit(parseContext);
    }

    @Override
    public void visitBitSignedShiftRightOperator(@NotNull BitSignedShiftRightOperator bitShr,
                                                 @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, bitShr).visit(parseContext);
    }

    @Override
    public void visitBitUnsignedShiftRightOperator(@NotNull BitUnsignedShiftRightOperator bitUshr,
                                                   @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, bitUshr).visit(parseContext);
    }

    @Override
    public void visitLesser(@NotNull LesserOperator lesserOperator, @NotNull  ParseContext parseContext) {
        try {
            if (this.expression instanceof LiteralExpr) {
                final TypeVariableListNode visit = new TypeParameterListVisitor(lesserOperator).visit(parseContext);
                ((LiteralExpr) this.expression).setTypeParameterListNode(visit);
                parseContext.getLexer().nextToken().accept(this, parseContext);
                return;
            }
        } catch (Exception ignored) {

        }
        parseContext.getLexer().recover(lesserOperator);
        this.expression = new OpExpressionVisitor(this.expression, lesserOperator).visit(parseContext);
    }

    @Override
    public void visitOr(@NotNull OrOperator orOperator, @NotNull  ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, orOperator).visit(parseContext);
    }

    @Override
    public void visitAnd(@NotNull AndOperator andOperator, @NotNull  ParseContext parseContext) {
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
    public void visitDefaultValueOperator(@NotNull DefaultValueOperator defaultValueOperator,
                                          @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, defaultValueOperator).visit(parseContext);
    }

    @Override
    public void visitRem(@NotNull RemOperator remOperator, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, remOperator).visit(parseContext);
    }

    @Override
    public void visitEqual(@NotNull EqualOperator equalOperator, @NotNull  ParseContext parseContext) {
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
    public void visitNotEqual(NotEqualOperator notEqualOperator, ParseContext parseContext) {
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
            this.expression = new NegExpr(subtractOperator, new ExpressionVisitor(parseContext
                    .getLexer()
                    .nextToken())
                    .visit(parseContext));
            goFurther(parseContext);
        } else {
            this.expression = new OpExpressionVisitor(this.expression, subtractOperator).visit(parseContext);
        }
    }

    @Override
    public void visitNotOperator(@NotNull NotOperator notOperator, @NotNull ParseContext parseContext) {
        this.expression = new NotExpr(notOperator, new ExpressionVisitor(parseContext
                .getLexer()
                .nextToken())
                .visit(parseContext));
        goFurther(parseContext);
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
    public void visitDot(@NotNull DotToken dotToken,
                         @NotNull ParseContext parseContext) {

        var createdExpr = new ReferenceExpressionImpl(
                this.expression == null ? EmptyExpression.INSTANCE : this.expression,
                dotToken);
        this.expression = createdExpr;
        createdExpr
                .setFurtherExpression(new ReferenceExpressionVisitor(parseContext.getLexer().nextToken())
                        .visit(parseContext));
        goFurther(parseContext, false);
    }

    @Override
    public void visitNullSafeCallOperator(@NotNull NullSafeCallOperator nullSafeCallOperator,
                                          @NotNull ParseContext parseContext) {

        var createdExpr = new NullSafeCallExprImpl(
                this.expression == null ? EmptyExpression.INSTANCE : this.expression,
                nullSafeCallOperator);
        this.expression = createdExpr;
        createdExpr
                .setFurtherExpression(new ReferenceExpressionVisitor(parseContext.getLexer().nextToken())
                        .visit(parseContext));
        goFurther(parseContext, false);
    }

    @Override
    public void visit(@NotNull DecrOperator decrOperator, @NotNull ParseContext parseContext) {
        this.expression= new RightHandExpressionVisitor(decrOperator).visit(parseContext);
        goFurther(parseContext);
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
            this.expression = new ArrayOrMapGetExpr(this.expression,
                    leftBracketToken,
                    expr,
                    ((RightBracketToken) lexer.getCurrent()));
            goFurther(parseContext);
            return;
        }
        var args = new ArrayList<Expression>();
        while (lexer.hasNextToken()) {
            var next = lexer.nextToken();
            if (next.getTokenType() == ElementType.rightBracket) {
                break;
            }
            var visit = new ExpressionVisitor(next).visit(parseContext);
            args.add(visit);
            if (lexer.getCurrent().getTokenType() == ElementType.rightBracket) {
                break;
            }
        }
        var right = (RightBracketToken) lexer.getCurrent();
        var peeked = lexer.peekNext();

        if (TokenUtils.isNewLine(parseContext, right.getEndOffset(), peeked.getStartOffset()) || !peeked.canBeType()) {
            this.expression = new ArrayCreateExpr(leftBracketToken, args, right);
            goFurther(parseContext);
            return;
        }

        var nextToken = lexer.nextToken();
        if (!(nextToken instanceof LiteralToken) && !(lexer.peekNext() instanceof RightBracketToken)) {
            this.expression = new ArrayCreateExpr(leftBracketToken, args, right);
            goFurther(parseContext, false);
            return;
        }

        var typeNodeVisitor = new TypeNodeVisitor(nextToken);
        try {
            var typeNode = typeNodeVisitor.visit(parseContext);
            this.expression = new TypedArrayCreateExpr(leftBracketToken, args, right, typeNode);
            goFurther(parseContext);
        } catch (Exception e) {
            if (lexer.getCurrent() instanceof LeftBracketToken) {
                this.expression = new TypedArrayCreateExpr(leftBracketToken, args, right, typeNodeVisitor.getTypeNode());
                goFurther(parseContext, false);
            } else  if (lexer.getCurrent() instanceof LeftParenthesisToken) {
                var sizeExpr = new ExpressionVisitor(lexer.nextToken()).visit(parseContext);
                this.expression = new TypedArrayCreateWithSizeExprTyped(leftBracketToken,
                        right,
                        typeNodeVisitor.getTypeNode(),
                        sizeExpr);
                goFurther(parseContext);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void visit(@NotNull NullToken nullToken, @NotNull ParseContext parseContext) {
        this.expression = new NullExpr(nullToken);
        goFurther(parseContext);
    }

    @Override
    public void visitTrue(@NotNull TrueToken trueToken, @NotNull ParseContext parseContext) {
        this.expression = new TrueNode(trueToken);
        goFurther(parseContext);
    }

    @Override
    public void visitFalse(@NotNull FalseToken falseToken, @NotNull ParseContext parseContext) {
        this.expression = new FalseNode(falseToken);
        goFurther(parseContext);
    }

    @Override
    public void visitForKeyword(@NotNull ForKeyword forKeyword, @NotNull ParseContext parseContext) {
        var expr = new ExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext);
        if (expr instanceof InExpr) {
            var inExpr = ((InExpr) expr);
            var varExpr = inExpr.getLeft();
            var inKeyword = inExpr.getInKeyword();
            var iterExpr = inExpr.getRight();
            var left = parseContext.getLexer().nextToken();
            var codeBlock = new BlockVisitor(left).visit(parseContext);
            this.expression = new ForEachLoop(forKeyword,
                    new LoopVar(((LiteralExpr) varExpr)),
                    inKeyword,
                    iterExpr,
                    left,
                    codeBlock,
                    parseContext.getLexer().getCurrent());
        } else {
            throw new SyntaxException("Expected in expression", expr, parseContext.getCurrentFilePath());
        }
    }

    @Override
    public void visitWhileKeyword(@NotNull WhileKeyword whileKeyword, @NotNull ParseContext parseContext) {
        var expr = new ExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext);
        var left = parseContext.getLexer().nextToken();
        if (left.getTokenType() != ElementType.leftCurl) {
            TokenUtils.checkExpectedToken(ElementType.leftCurl, left, "{", parseContext);
        }
        var codeBlock = new BlockVisitor(left).visit(parseContext);
        var right = parseContext.getLexer().getCurrent();
        if (right.getTokenType() != ElementType.rightCurl) {
            TokenUtils.checkExpectedToken(ElementType.rightCurl, right, "}", parseContext);
        }
        this.expression = new WhileLoopImpl(
                whileKeyword,
                new WhileConditionExpressionWrapperImpl(expr),
                left,
                codeBlock,
                right
        );
    }

    @Override
    public void visitArrowToken(@NotNull ArrowToken arrowToken, @NotNull ParseContext parseContext) {

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
    public void visitRightParenthesis(@NotNull RightParenthesisToken rightParenthesisToken,
                                      @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitColon(@NotNull ColonToken colonToken, @NotNull ParseContext parseContext) {
        this.expression = new OpExpressionVisitor(this.expression, colonToken).visit(parseContext);
    }

    @Override
    public void visitInKeyword(@NotNull InKeyword inKeyword, @NotNull ParseContext parseContext) {
        checkLeftExpression(inKeyword, parseContext);
        this.expression = new OpExpressionVisitor(this.expression, inKeyword).visit(parseContext);
    }

    @Override
    public void visitBreakKeyword(@NotNull BreakKeyword breakKeyword, @NotNull ParseContext parseContext) {
        this.expression = new BreakExprImpl(breakKeyword);
    }

    @Override
    public void visitContinueKeyword(@NotNull ContinueKeyword continueKeyword, @NotNull ParseContext parseContext) {
        this.expression = new ContinueExprImpl(continueKeyword);
    }

    private void checkLeftExpression(@NotNull AbstractOperator op,
                                     @NotNull ParseContext parseContext) {
        if (this.expression == null) {
            throw new SyntaxException("Expected left expression for " + op.getText() + " operator",
                    op,
                    parseContext.getCurrentFilePath());
        }
    }

    private void goFurther(@NotNull ParseContext parseContext) {
        goFurther(parseContext, true);
    }

    private void goFurther(@NotNull ParseContext parseContext, boolean nextToken) {
        Token peekedNext = parseContext.getLexer().peekNext();

        switch (peekedNext.getTokenType()) {
            case returnKeyword:
            case valKeyword:
            case varKeyword:
            case breakKeyword:
            case continueKeyword:
            case ifKeyword:
                ASTUtils.checkNewLine(parseContext, this.expression, peekedNext);
            case leftCurl:
            case rightCurl:
            case elseKeyword:
            case elifKeyword:
                return;
        }

        if (TokenUtils.isNewLine(parseContext, this.expression.getEndOffset(), peekedNext.getStartOffset())) {
            return;
        }

        if (nextToken) {
            parseContext.getLexer().nextToken().accept(this, parseContext);
        } else {
            parseContext.getLexer().getCurrent().accept(this, parseContext);
        }
    }
}
