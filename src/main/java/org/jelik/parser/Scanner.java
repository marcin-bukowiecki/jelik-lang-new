package org.jelik.parser;

import org.jelik.parser.token.*;
import org.jelik.parser.token.keyword.*;
import org.jelik.parser.token.operators.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class Scanner {

    /* parsed tokens */
    protected List<Token> tokens = new ArrayList<>();

    /* current index of tokens in ArrayList */
    private int pos = 0;

    private final CharPointer cp;

    public Scanner(CharPointer charPointer) {
        this.cp = charPointer;
        cp.nextChar(); //init char pointer
    }

    public List<Token> getTokens() {
        return tokens;
    }

    @NotNull
    public CharPointer getCp() {
        return cp;
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
            if (t.getStartOffset() == to.getStartOffset() && to.equals(t)) {
                pos++;
                return;
            } else {
                pos--;
            }
        }
    }

    private Token loopUntilNextToken() {
        int startOffset = cp.getOffset();

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
                    nextToken = new SingleApostropheToken(startOffset);
                    break breakable;
                case '\"':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new ApostropheToken(startOffset);
                    break breakable;
                case '!':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new NotEqualOperator(startOffset);
                        break breakable;
                    }
                    nextToken = new NotOperator(startOffset);
                    break breakable;
                case '?':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '.') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new NullSafeCallOperator(startOffset);
                        break breakable;
                    } else if (nextChar == ':') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new DefaultValueOperator(startOffset);
                        break breakable;
                    }
                    nextToken = new QuestionMarkToken(startOffset);
                    break breakable;
                case '=':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new EqualOperator(startOffset);
                        break breakable;
                    }
                    nextToken = new AssignOperator(startOffset);
                    break breakable;
                case ':':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new ColonToken(startOffset);
                    break breakable;
                case '+':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '+') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new IncrOperator(startOffset);
                        break breakable;
                    }
                    nextToken = new AddOperator(startOffset);
                    break breakable;
                case '*':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new MulOperator(startOffset);
                    break breakable;
                case '/':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new DivideOperator(startOffset);
                    break breakable;
                case '%':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RemOperator(startOffset);
                    break breakable;
                case '.':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new DotToken(startOffset);
                    break breakable;
                case '<':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new LesserOrEqualOperator(startOffset);
                        break breakable;
                    }
                    nextToken = new LesserOperator(startOffset);
                    break breakable;
                case '>':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    if (nextChar == '=') {
                        nextChar = (char) cp.nextChar();
                        nextToken = new GreaterOrEqualOperator(startOffset);
                        break breakable;
                    }
                    nextToken = new GreaterOperator(startOffset);
                    break breakable;
                case '[':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new LeftBracketToken(startOffset);
                    break breakable;
                case ']':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RightBracketToken(startOffset);
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
                            nextToken = new ArrowToken(startOffset);
                            break breakable;
                        case '-':
                            nextChar = (char) cp.nextChar();
                            nextToken = new DecrOperator(startOffset);
                            break breakable;
                        default:
                            nextToken = new SubtractOperator(startOffset);
                            break breakable;
                    }
                case ',':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new CommaToken(startOffset);
                    break breakable;
                case '\n':
                case '\r':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new NewLineToken(startOffset);
                    break breakable;
                case ')':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RightParenthesisToken(startOffset);
                    break breakable;
                case '(':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new LeftParenthesisToken(startOffset);
                    break breakable;
                case '}':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new RightCurlToken(startOffset);
                    break breakable;
                case '{':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new LeftCurlToken(startOffset);
                    break breakable;
                case '|':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new PipeToken(startOffset);
                    break breakable;
                case '\u0020':
                case '\u0009':
                case '\u000C':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    nextChar = (char) cp.nextChar();
                    nextToken = new WhitespaceToken(startOffset);
                    break breakable;
                case '\uFFFF':
                    if (sb.length() > 0) {
                        break breakable;
                    }
                    return new EofTok(startOffset);
                default:
                    sb.append(nextChar);
            }

            nextChar = (char) cp.nextChar();
        }

        if (nextToken != null) {
            return nextToken;
        }

        return resolveBuiltInToken(sb.toString(), startOffset);
    }

    private Token resolveBuiltInToken(String content, int offset) {
        if ("fun".equals(content)) {
            return new FunKeyword(offset);
        } else if ("met".equals(content)) {
            return new MetKeyword(offset);
        } else if ("abstract".equals(content)) {
            return new AbstractKeyword(offset);
        } else if ("interface".equals(content)) {
            return new InterfaceKeyword(offset);
        } else if ("ret".equals(content)) {
            return new ReturnKeyword(offset);
        } else if ("import".equals(content)) {
            return new ImportKeyword(offset);
        } else if ("val".equals(content)) {
            return new ValKeyword(offset);
        } else if ("var".equals(content)) {
            return new VarKeyword(offset);
        } else if ("if".equals(content)) {
            return new IfKeyword(offset);
        } else if ("else".equals(content)) {
            return new ElseKeyword(offset);
        } else if ("elif".equals(content)) {
            return new ElifKeyword(offset);
        } else if ("true".equals(content)) {
            return new TrueToken(offset);
        } else if ("false".equals(content)) {
            return new FalseToken(offset);
        } else if ("Null".equals(content)) {
            return new NullToken(offset);
        } else if ("or".equals(content)) {
            return new OrOperator(offset);
        } else if ("and".equals(content)) {
            return new AndOperator(offset);
        } else if ("throw".equals(content)) {
            return new ThrowKeyword(offset);
        } else if ("finally".equals(content)) {
            return new FinallyKeyword(offset);
        } else if ("catch".equals(content)) {
            return new CatchKeyword(offset);
        } else if ("try".equals(content)) {
            return new TryKeyword(offset);
        } else if ("as".equals(content)) {
            return new AsOperator(offset);
        } else if ("band".equals(content)) {
            return new BitAndOperator(offset);
        } else if ("bor".equals(content)) {
            return new BitOrOperator(offset);
        } else if ("xor".equals(content)) {
            return new BitXorOperator(offset);
        } else if ("is".equals(content)) {
            return new IsOperator(offset);
        } else if ("class".equals(content)) {
            return new ClassKeyword(offset);
        } else if ("static".equals(content)) {
            return new StaticKeyword(offset);
        } else if ("constructor".equals(content)) {
            return new ConstructorKeyword(offset);
        } else if ("super".equals(content)) {
            return new SuperKeyword(offset);
        } else if ("ushr".equals(content)) {
            return new BitUnsignedShiftRightOperator(offset);
        } else if ("shr".equals(content)) {
            return new BitSignedShiftRightOperator(offset);
        } else if ("shl".equals(content)) {
            return new BitSignedShiftLeftOperator(offset);
        } else if ("ext".equals(content)) {
            return new ExtKeyword(offset);
        } else if ("for".equals(content)) {
            return new ForKeyword(offset);
        } else if ("while".equals(content)) {
            return new WhileKeyword(offset);
        } else if ("in".equals(content)) {
            return new InKeyword(offset);
        } else if ("break".equals(content)) {
            return new BreakKeyword(offset);
        } else if ("continue".equals(content)) {
            return new ContinueKeyword(offset);
        } else if ("package".equals(content)) {
            return new PackageKeyword(offset);
        } else if ("lam".equals(content)) {
            return new LamKeyword(offset);
        } else if ("object".equals(content)) {
            return new ObjectKeyword(offset);
        } else if ("when".equals(content)) {
            return new WhenKeyword(offset);
        } else if ("operator".equals(content)) {
            return new OperatorKeyword(offset);
        }

        return new LiteralToken(offset, content);
    }
}
