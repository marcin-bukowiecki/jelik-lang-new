package org.jelik.parser.ast;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.ParseContext;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.ArrowToken;
import org.jelik.parser.token.ColonToken;
import org.jelik.parser.token.CommaToken;
import org.jelik.parser.token.DotToken;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.PipeToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.*;
import org.jelik.parser.token.LeftBracketToken;
import org.jelik.parser.token.LeftCurlToken;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.NewLineToken;
import org.jelik.parser.token.NullToken;
import org.jelik.parser.token.QuestionMarkToken;
import org.jelik.parser.token.RightBracketToken;
import org.jelik.parser.token.RightCurlToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jelik.parser.token.SingleApostropheToken;
import org.jelik.parser.token.WhitespaceToken;
import org.jelik.parser.token.operators.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 *
 * @param <T> parsed expression
 */
public interface TokenVisitor<T extends ASTNode> {

    @NotNull
    T visit(@NotNull ParseContext parseContext);

    default void visitToken(@NotNull Token token, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", token, parseContext.getCurrentFilePath());
    }

    default void visitOp(@NotNull AbstractOperator abstractOperator, @NotNull ParseContext parseContext) {
        visitToken(abstractOperator, parseContext);
    }

    default void visitWhitespace(@NotNull WhitespaceToken wt, @NotNull ParseContext parseContext) {

    }

    default void visitFunKeyword(@NotNull FunKeyword funKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", funKeyword, parseContext.getCurrentFilePath());
    }

    default void visitClassKeyword(@NotNull ClassKeyword classKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", classKeyword, parseContext.getCurrentFilePath());
    }

    default void visitImportKeyword(@NotNull ImportKeyword importKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", importKeyword, parseContext.getCurrentFilePath());
    }

    default void visitLeftParenthesis(@NotNull LeftParenthesisToken leftParenthesisToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", leftParenthesisToken, parseContext.getCurrentFilePath());
    }

    default void visitLeftCurl(@NotNull LeftCurlToken leftCurlToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", leftCurlToken, parseContext.getCurrentFilePath());
    }

    default void visitRightCurl(@NotNull RightCurlToken rightCurlToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", rightCurlToken, parseContext.getCurrentFilePath());
    }

    default void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", literalToken, parseContext.getCurrentFilePath());
    }

    default void visitAdd(@NotNull AddOperator addOperator, @NotNull ParseContext parseContext) {
        visitOp(addOperator, parseContext);
    }

    default void visitReturnKeyword(@NotNull ReturnKeyword returnKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", returnKeyword, parseContext.getCurrentFilePath());
    }

    default void visitArrowToken(@NotNull ArrowToken arrowToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", arrowToken, parseContext.getCurrentFilePath());
    }

    default void visitNewLine(@NotNull NewLineToken newLineToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", newLineToken, parseContext.getCurrentFilePath());
    }

    default void visitPackageKeyword(@NotNull PackageKeyword packageKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", packageKeyword, parseContext.getCurrentFilePath());
    }

    default void visitDot(@NotNull DotToken dotToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", dotToken, parseContext.getCurrentFilePath());
    }

    default void visitRightParenthesis(@NotNull RightParenthesisToken rightParenthesisToken,
                                       @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", rightParenthesisToken, parseContext.getCurrentFilePath());
    }

    default void visitColon(@NotNull ColonToken colonToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", colonToken, parseContext.getCurrentFilePath());
    }

    default void visitSubtract(@NotNull SubtractOperator subtractOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", subtractOperator, parseContext.getCurrentFilePath());
    }

    default void visitDivide(@NotNull DivideOperator divideOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", divideOperator, parseContext.getCurrentFilePath());
    }

    default void visitMul(@NotNull MulOperator op, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", op, parseContext.getCurrentFilePath());
    }

    default void visitValKeyword(@NotNull ValKeyword valKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", valKeyword, parseContext.getCurrentFilePath());
    }

    default void visitVarKeyword(@NotNull VarKeyword varKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", varKeyword, parseContext.getCurrentFilePath());
    }

    default void visitIfKeyword(@NotNull IfKeyword ifKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", ifKeyword, parseContext.getCurrentFilePath());
    }

    default void visitElifKeyword(ElifKeyword elifKeyword, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", elifKeyword, parseContext.getCurrentFilePath());
    }

    default void visitElseKeyword(ElseKeyword elseKeyword, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", elseKeyword, parseContext.getCurrentFilePath());
    }

    default void visitAssign(AssignOperator assignOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", assignOperator, parseContext.getCurrentFilePath());
    }

    default void visit(ApostropheToken apostropheToken, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", apostropheToken, parseContext.getCurrentFilePath());
    }

    default void visit(EofTok eofTok, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", eofTok, parseContext.getCurrentFilePath());
    }

    default void visitGreater(GreaterOperator greaterOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", greaterOperator, parseContext.getCurrentFilePath());
    }

    default void visit(IncrOperator incrOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", incrOperator, parseContext.getCurrentFilePath());
    }

    default void visit(DecrOperator decrOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", decrOperator, parseContext.getCurrentFilePath());
    }

    default void visitNotEqual(NotEqualOperator notEqualOperator, ParseContext parseContext) {
        visitOp(notEqualOperator, parseContext);
    }

    default void visitLesser(@NotNull LesserOperator lesserOperator, @NotNull ParseContext parseContext) {
        visitOp(lesserOperator, parseContext);
    }

    default void visitTrue(@NotNull TrueToken trueToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", trueToken, parseContext.getCurrentFilePath());
    }

    default void visitFalse(@NotNull FalseToken falseToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", falseToken, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull NullToken nullToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", nullToken, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull LeftBracketToken leftBracketToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", leftBracketToken, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull RightBracketToken rightBracketToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", rightBracketToken, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull CommaToken commaToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", commaToken, parseContext.getCurrentFilePath());
    }

    default void visitOr(@NotNull OrOperator orOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", orOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull ThrowKeyword throwKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", throwKeyword, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull TryKeyword tryKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", tryKeyword, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull CatchKeyword catchKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", catchKeyword, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull FinallyKeyword finallyKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", finallyKeyword, parseContext.getCurrentFilePath());
    }

    default void visitAs(@NotNull AsOperator asOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", asOperator, parseContext.getCurrentFilePath());
    }

    default void visitAnd(@NotNull AndOperator andOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", andOperator, parseContext.getCurrentFilePath());
    }

    default void visitRem(@NotNull RemOperator remOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", remOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull GreaterOrEqualOperator greaterOrEqualOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", greaterOrEqualOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull LesserOrEqualOperator lesserOrEqualOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", lesserOrEqualOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull BitAndOperator bitAndOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", bitAndOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull BitOrOperator bitOrOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", bitOrOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull BitXorOperator bitXorOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", bitXorOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull IsOperator isOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", isOperator, parseContext.getCurrentFilePath());
    }

    default void visitConstructorKeyword(@NotNull ConstructorKeyword constructorKeyword,
                                         @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", constructorKeyword, parseContext.getCurrentFilePath());
    }

    default void visitStaticKeyword(@NotNull StaticKeyword staticKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", staticKeyword, parseContext.getCurrentFilePath());
    }

    default void visitQuestionMark(@NotNull QuestionMarkToken questionMarkToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", questionMarkToken, parseContext.getCurrentFilePath());
    }

    default void visitSuperKeyword(@NotNull SuperKeyword superKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", superKeyword, parseContext.getCurrentFilePath());
    }

    default void visitBitUnsignedShiftRightOperator(@NotNull BitUnsignedShiftRightOperator bitUshr,
                                                    @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", bitUshr, parseContext.getCurrentFilePath());
    }

    default void visitBitSignedShiftRightOperator(@NotNull BitSignedShiftRightOperator bitShr,
                                                  @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", bitShr, parseContext.getCurrentFilePath());
    }

    default void visitBitSignedShiftLeftOperator(@NotNull BitSignedShiftLeftOperator bitShl,
                                                 @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", bitShl, parseContext.getCurrentFilePath());
    }

    default void visitForKeyword(@NotNull ForKeyword forKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", forKeyword, parseContext.getCurrentFilePath());
    }

    default void visitExtKeyword(@NotNull ExtKeyword extKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", extKeyword, parseContext.getCurrentFilePath());
    }

    default void visitWhileKeyword(@NotNull WhileKeyword whileKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", whileKeyword, parseContext.getCurrentFilePath());
    }

    default void visitInKeyword(@NotNull InKeyword inKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", inKeyword, parseContext.getCurrentFilePath());
    }

    default void visitContinueKeyword(@NotNull ContinueKeyword continueKeyword, @NotNull ParseContext parseContext) {
        visitToken(continueKeyword, parseContext);
    }

    default void visitBreakKeyword(@NotNull BreakKeyword breakKeyword, @NotNull ParseContext parseContext) {
        visitToken(breakKeyword, parseContext);
    }

    default void visitLamKeyword(@NotNull LamKeyword lamKeyword, @NotNull ParseContext parseContext) {
        visitToken(lamKeyword, parseContext);
    }

    default void visitSingleApostropheToken(@NotNull SingleApostropheToken singleApostropheToken,
                                            @NotNull ParseContext parseContext) {
        visitToken(singleApostropheToken, parseContext);
    }

    default void visitNotOperator(@NotNull NotOperator notOperator, @NotNull ParseContext parseContext) {
        visitToken(notOperator, parseContext);
    }

    default void visitNullSafeCallOperator(@NotNull NullSafeCallOperator nullSafeCallOperator,
                                           @NotNull ParseContext parseContext) {
        visitToken(nullSafeCallOperator, parseContext);
    }

    default void visitEqual(@NotNull EqualOperator equalOperator, @NotNull ParseContext parseContext) {
        visitOp(equalOperator, parseContext);
    }

    default void visitInterfaceKeyword(@NotNull InterfaceKeyword interfaceKeyword, @NotNull ParseContext parseContext) {
        visitToken(interfaceKeyword, parseContext);
    }

    default void visitMetKeyword(@NotNull MetKeyword metKeyword, @NotNull ParseContext parseContext) {
        visitToken(metKeyword, parseContext);
    }

    default void visitPublicKeyword(@NotNull PublicKeyword publicKeyword, @NotNull ParseContext parseContext) {
        visitToken(publicKeyword, parseContext);
    }

    default void visitPrivateKeyword(@NotNull PrivateKeyword privateKeyword, @NotNull ParseContext parseContext) {
        visitToken(privateKeyword, parseContext);
    }

    default void visitAbstractKeyword(@NotNull  AbstractKeyword abstractKeyword, @NotNull ParseContext parseContext) {
        visitToken(abstractKeyword, parseContext);
    }

    default void visitPipe(@NotNull PipeToken pipeToken, @NotNull ParseContext parseContext) {
        visitToken(pipeToken, parseContext);
    }

    default void visitWhenKeyword(@NotNull WhenKeyword whenKeyword, @NotNull ParseContext parseContext) {
        visitToken(whenKeyword, parseContext);
    }

    default void visitOperatorKeyword(@NotNull OperatorKeyword operatorKeyword, @NotNull ParseContext parseContext) {
        visitToken(operatorKeyword, parseContext);
    }

    default void visitObjectKeyword(@NotNull ObjectKeyword objectKeyword, @NotNull ParseContext parseContext) {
        visitToken(objectKeyword, parseContext);
    }

    default void visitDefaultValueOperator(@NotNull DefaultValueOperator defaultValueOperator,
                                           @NotNull ParseContext parseContext) {
        visitToken(defaultValueOperator, parseContext);
    }

    default void visitRecordKeyword(@NotNull RecordKeyword recordKeyword,
                                    @NotNull ParseContext parseContext) {
        visitToken(recordKeyword, parseContext);
    }
}
