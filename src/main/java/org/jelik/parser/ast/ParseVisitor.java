package org.jelik.parser.ast;

import org.jelik.parser.ParseContext;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.ArrowToken;
import org.jelik.parser.token.ColonToken;
import org.jelik.parser.token.CommaToken;
import org.jelik.parser.token.DotToken;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.FalseToken;
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
import org.jelik.parser.token.TrueToken;
import org.jelik.parser.token.WhitespaceToken;
import org.jelik.parser.token.keyword.CatchKeyword;
import org.jelik.parser.token.keyword.ClassKeyword;
import org.jelik.parser.token.keyword.ConstructorKeyword;
import org.jelik.parser.token.keyword.ElifKeyword;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jelik.parser.token.keyword.FinallyKeyword;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ImportKeyword;
import org.jelik.parser.token.keyword.PackageKeyword;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.keyword.StaticKeyword;
import org.jelik.parser.token.keyword.ThenKeyword;
import org.jelik.parser.token.keyword.ThrowKeyword;
import org.jelik.parser.token.keyword.TryKeyword;
import org.jelik.parser.token.keyword.ValKeyword;
import org.jelik.parser.token.keyword.VarKeyword;
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

/**
 * @author Marcin Bukowiecki
 *
 * @param <T> parsed expression
 */
public interface ParseVisitor<T extends ASTNode> {

    @NotNull
    T visit(@NotNull ParseContext parseContext);

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
        throw new SyntaxException("Unexpected token", addOperator, parseContext.getCurrentFilePath());
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

    default void visit(ThenKeyword thenKeyword, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", thenKeyword, parseContext.getCurrentFilePath());
    }

    default void visit(EndKeyword endKeyword, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", endKeyword, parseContext.getCurrentFilePath());
    }

    default void visit(ApostropheToken apostropheToken, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", apostropheToken, parseContext.getCurrentFilePath());
    }

    default void visit(EofTok eofTok, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", eofTok, parseContext.getCurrentFilePath());
    }

    default void visit(GreaterOperator greaterOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", greaterOperator, parseContext.getCurrentFilePath());
    }

    default void visit(EqualOperator equalOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", equalOperator, parseContext.getCurrentFilePath());
    }

    default void visit(IncrOperator incrOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", incrOperator, parseContext.getCurrentFilePath());
    }

    default void visit(DecrOperator decrOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", decrOperator, parseContext.getCurrentFilePath());
    }

    default void visit(NotEqualOperator notEqualOperator, ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", notEqualOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull LesserOperator lesserOperator, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", lesserOperator, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull TrueToken trueToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", trueToken, parseContext.getCurrentFilePath());
    }

    default void visit(@NotNull FalseToken falseToken, @NotNull ParseContext parseContext) {
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

    default void visit(@NotNull OrOperator orOperator, @NotNull ParseContext parseContext) {
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

    default void visit(@NotNull AndOperator andOperator, @NotNull ParseContext parseContext) {
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

    default void visitConstructorKeyword(@NotNull ConstructorKeyword constructorKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", constructorKeyword, parseContext.getCurrentFilePath());
    }

    default void visitStaticKeyword(@NotNull StaticKeyword staticKeyword, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", staticKeyword, parseContext.getCurrentFilePath());
    }

    default void visitQuestionMark(@NotNull QuestionMarkToken questionMarkToken, @NotNull ParseContext parseContext) {
        throw new SyntaxException("Unexpected token", questionMarkToken, parseContext.getCurrentFilePath());
    }
}
