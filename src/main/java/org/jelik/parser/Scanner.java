package org.jelik.parser;

import lombok.Getter;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.ArrowToken;
import org.jelik.parser.token.ColonToken;
import org.jelik.parser.token.CommaToken;
import org.jelik.parser.token.DotToken;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.PipeToken;
import org.jelik.parser.token.keyword.FalseToken;
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
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ObjectKeyword;
import org.jelik.parser.token.keyword.OperatorKeyword;
import org.jelik.parser.token.keyword.TrueToken;
import org.jelik.parser.token.WhitespaceToken;
import org.jelik.parser.token.keyword.AbstractKeyword;
import org.jelik.parser.token.keyword.BreakKeyword;
import org.jelik.parser.token.keyword.CatchKeyword;
import org.jelik.parser.token.keyword.ClassKeyword;
import org.jelik.parser.token.keyword.ConstructorKeyword;
import org.jelik.parser.token.keyword.ContinueKeyword;
import org.jelik.parser.token.keyword.DoKeyword;
import org.jelik.parser.token.keyword.ElifKeyword;
import org.jelik.parser.token.keyword.ElseKeyword;
import org.jelik.parser.token.keyword.EndKeyword;
import org.jelik.parser.token.keyword.ExtKeyword;
import org.jelik.parser.token.keyword.FinallyKeyword;
import org.jelik.parser.token.keyword.ForKeyword;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ImportKeyword;
import org.jelik.parser.token.keyword.InKeyword;
import org.jelik.parser.token.keyword.InterfaceKeyword;
import org.jelik.parser.token.keyword.LamKeyword;
import org.jelik.parser.token.keyword.MetKeyword;
import org.jelik.parser.token.keyword.PackageKeyword;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.keyword.StaticKeyword;
import org.jelik.parser.token.keyword.SuperKeyword;
import org.jelik.parser.token.keyword.ThenKeyword;
import org.jelik.parser.token.keyword.ThrowKeyword;
import org.jelik.parser.token.keyword.TryKeyword;
import org.jelik.parser.token.keyword.ValKeyword;
import org.jelik.parser.token.keyword.VarKeyword;
import org.jelik.parser.token.keyword.WhenKeyword;
import org.jelik.parser.token.keyword.WhileKeyword;
import org.jelik.parser.token.operators.AddOperator;
import org.jelik.parser.token.operators.AndOperator;
import org.jelik.parser.token.operators.AsOperator;
import org.jelik.parser.token.operators.AssignOperator;
import org.jelik.parser.token.operators.BitAndOperator;
import org.jelik.parser.token.operators.BitOrOperator;
import org.jelik.parser.token.operators.BitSignedShiftLeftOperator;
import org.jelik.parser.token.operators.BitSignedShiftRightOperator;
import org.jelik.parser.token.operators.BitUnsignedShiftRightOperator;
import org.jelik.parser.token.operators.BitXorOperator;
import org.jelik.parser.token.operators.DecrOperator;
import org.jelik.parser.token.operators.DefaultValueOperator;
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
import org.jelik.parser.token.operators.NotOperator;
import org.jelik.parser.token.operators.NullSafeCallOperator;
import org.jelik.parser.token.operators.OrOperator;
import org.jelik.parser.token.operators.RemOperator;
import org.jelik.parser.token.operators.SubtractOperator;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class Scanner {

    /* parsed tokens */
    @Getter
    protected List<Token> tokens = new ArrayList<>();

    /* current index of tokens in ArrayList */
    private int pos = 0;

    @Getter
    private final CharPointer cp;

    public Scanner(CharPointer charPointer) {
        this.cp = charPointer;
        cp.nextChar(); //init char pointer
    }

    @VisibleForTesting
    public Scanner(File file) {
        this.cp = new CharPointer(file);
        this.cp.nextChar();
    }

    /**
     * @return true if there is another token, otherwise false
     */
    public boolean hasNext() {
        //used by recover and whitespaces
        if (pos <= tokens.size() - 1) {
            return true;
        }

        return cp.hasNext();
    }

    /**
     * @return next token (with spaces etc.)
     */
    public Token next() {
        //in recovery mode
        if (pos <= tokens.size() - 1) {
            return tokens.get(pos++);
        }

        Token token = loopUntilNextToken();
        tokens.add(token);
        pos++;
        return token;
    }

    /**
     * Recover to given token (i.e. when we have a generic type someExpr<Type> but we previously marked it as a
     * logical expression someExpr < Type.
     *
     * @param to Token to which we are recovering
     */
    void recover(Token to) {
        pos--;
        while (pos > 0) {
            Token t = tokens.get(pos);
            if (t.getRow() == to.getRow() && t.getCol() == to.getCol() && to.equals(t)) {
                pos++;
                return;
            } else {
                pos--;
            }
        }
    }

    private Token loopUntilNextToken() {
        int row = cp.getLineNumber();
        int col = cp.getColumnNumber();

        var sb = new StringBuilder();
        char nextChar = (char) cp.current();

        Token nextToken = null;

        breakable: while (true) {
            switch (nextChar) {
                case '\'':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new SingleApostropheToken(row, col);
                    break breakable;
                case '\"':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new ApostropheToken(row, col);
                    break breakable;
                case '!':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new NotEqualOperator(cp.getLineNumber(), col);
                        break breakable;
                    }
                    nextToken = new NotOperator(cp.getLineNumber(), col);
                    break breakable;
                case '?':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '.') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new NullSafeCallOperator(cp.getLineNumber(), col);
                        break breakable;
                    } else if (nextChar == ':') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new DefaultValueOperator(cp.getLineNumber(), col);
                        break breakable;
                    }
                    nextToken = new QuestionMarkToken(cp.getLineNumber(), col);
                    break breakable;
                case '=':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new EqualOperator(cp.getLineNumber(), col);
                        break breakable;
                    }
                    nextToken = new AssignOperator(cp.getLineNumber(), col);
                    break breakable;
                case ':':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new ColonToken(row, col);
                    break breakable;
                case '+':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '+') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new IncrOperator(cp.getLineNumber(), col);
                        break breakable;
                    }
                    nextToken = new AddOperator(cp.getLineNumber(), col);
                    break breakable;
                case '*':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new MulOperator(row, col);
                    break breakable;
                case '/':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new DivideOperator(row, col);
                    break breakable;
                case '%':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RemOperator(row, col);
                    break breakable;
                case '.':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new DotToken(row, col);
                    break breakable;
                case '<':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new LesserOrEqualOperator(cp.getLineNumber(), col);
                        break breakable;
                    }
                    nextToken = new LesserOperator(cp.getLineNumber(), col);
                    break breakable;
                case '>':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new GreaterOrEqualOperator(cp.getLineNumber(), col);
                        break breakable;
                    }
                    nextToken = new GreaterOperator(cp.getLineNumber(), col);
                    break breakable;
                case '[':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new LeftBracketToken(row, col);
                    break breakable;
                case ']':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RightBracketToken(row, col);
                    break breakable;
                case '-':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    switch (nextChar) {
                        //is -> (arrow)
                        case '>':
                            nextChar = (char) cp.nextChar();
                            nextToken = new ArrowToken(cp.getLineNumber(), col);
                            break breakable;
                        case '-':
                            nextChar = (char) cp.nextChar();
                            nextToken = new DecrOperator(cp.getLineNumber(), col);
                            break breakable;
                        default:
                            nextToken = new SubtractOperator(cp.getLineNumber(), col);
                            break breakable;
                    }
                case ',':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new CommaToken(row, col);
                    break breakable;
                case '\n':
                case '\r':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new NewLineToken(row, col);
                    cp.incrLineNumber();
                    break breakable;
                case ')':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RightParenthesisToken(row, col);
                    break breakable;
                case '(':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new LeftParenthesisToken(row, col);
                    break breakable;
                case '}':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RightCurlToken(row, col);
                    break breakable;
                case '{':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new LeftCurlToken(row, col);
                    break breakable;
                case '|':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new PipeToken(row, col);
                    break breakable;
                case '\u0020':
                case '\u0009':
                case '\u000C':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new WhitespaceToken(row, col);
                    break breakable;
                case '\uFFFF':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    return new EofTok(row, col);
                default:
                    sb.append(nextChar);
            }

            nextChar = (char) cp.nextChar();
        }

        if (nextToken != null) {
            return nextToken;
        }

        return resolveBuiltInToken(sb.toString(), row, col);
    }

    private Token resolveBuiltInToken(String content, int row, int col) {
        if ("fun".equals(content)) {
            return new FunKeyword(row, col);
        } else if ("met".equals(content)) {
            return new MetKeyword(row, col);
        } else if ("abstract".equals(content)) {
            return new AbstractKeyword(row, col);
        } else if ("interface".equals(content)) {
            return new InterfaceKeyword(row, col);
        } else if ("ret".equals(content)) {
            return new ReturnKeyword(row, col);
        } else if ("import".equals(content)) {
            return new ImportKeyword(row, col);
        } else if ("val".equals(content)) {
            return new ValKeyword(row, col);
        } else if ("var".equals(content)) {
            return new VarKeyword(row, col);
        } else if ("if".equals(content)) {
            return new IfKeyword(row, col);
        } else if ("then".equals(content)) {
            return new ThenKeyword(row, col);
        } else if ("end".equals(content)) {
            return new EndKeyword(row, col);
        } else if ("else".equals(content)) {
            return new ElseKeyword(row, col);
        } else if ("elif".equals(content)) {
            return new ElifKeyword(row, col);
        } else if ("true".equals(content)) {
            return new TrueToken(row, col);
        } else if ("false".equals(content)) {
            return new FalseToken(row, col);
        } else if ("Null".equals(content)) {
            return new NullToken(row, col);
        } else if ("or".equals(content)) {
            return new OrOperator(row, col);
        } else if ("and".equals(content)) {
            return new AndOperator(row, col);
        } else if ("throw".equals(content)) {
            return new ThrowKeyword(row, col);
        } else if ("finally".equals(content)) {
            return new FinallyKeyword(row, col);
        } else if ("catch".equals(content)) {
            return new CatchKeyword(row, col);
        } else if ("try".equals(content)) {
            return new TryKeyword(row, col);
        } else if ("as".equals(content)) {
            return new AsOperator(row, col);
        } else if ("band".equals(content)) {
            return new BitAndOperator(row, col);
        } else if ("bor".equals(content)) {
            return new BitOrOperator(row, col);
        } else if ("xor".equals(content)) {
            return new BitXorOperator(row, col);
        } else if ("is".equals(content)) {
            return new IsOperator(row, col);
        } else if ("class".equals(content)) {
            return new ClassKeyword(row, col);
        } else if ("static".equals(content)) {
            return new StaticKeyword(row, col);
        } else if ("constructor".equals(content)) {
            return new ConstructorKeyword(row, col);
        } else if ("super".equals(content)) {
            return new SuperKeyword(row, col);
        } else if ("ushr".equals(content)) {
            return new BitUnsignedShiftRightOperator(row, col);
        } else if ("shr".equals(content)) {
            return new BitSignedShiftRightOperator(row, col);
        } else if ("shl".equals(content)) {
            return new BitSignedShiftLeftOperator(row, col);
        } else if ("ext".equals(content)) {
            return new ExtKeyword(row, col);
        } else if ("for".equals(content)) {
            return new ForKeyword(row, col);
        } else if ("while".equals(content)) {
            return new WhileKeyword(row, col);
        } else if ("in".equals(content)) {
            return new InKeyword(row, col);
        } else if ("do".equals(content)) {
            return new DoKeyword(row, col);
        } else if ("break".equals(content)) {
            return new BreakKeyword(row, col);
        } else if ("continue".equals(content)) {
            return new ContinueKeyword(row, col);
        } else if ("package".equals(content)) {
            return new PackageKeyword(row, col);
        } else if ("lam".equals(content)) {
            return new LamKeyword(row, col);
        } else if ("object".equals(content)) {
            return new ObjectKeyword(row, col);
        } else if ("when".equals(content)) {
            return new WhenKeyword(row, col);
        } else if ("operator".equals(content)) {
            return new OperatorKeyword(row, col);
        }

        return new LiteralToken(row, col, content);
    }
}
