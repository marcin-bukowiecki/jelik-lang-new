package org.jelik.parser;

import com.google.common.annotations.VisibleForTesting;
import org.jelik.parser.token.NewLineToken;
import org.jelik.parser.token.Token;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class Lexer {

    private final List<Integer> newLineOffSets = new ArrayList<>();

    private final Scanner scanner;

    private Token peeked = null;

    private Token current = null;

    public Lexer(Scanner scanner) {
        this.scanner = scanner;
    }

    public Lexer(String path) {
        this.scanner = new Scanner(new CharPointer(new File(path)));
    }

    @VisibleForTesting
    public static Lexer of(String expr) {
        return new Lexer(new Scanner(new CharPointer(expr)));
    }

    public boolean hasNextToken() {
        if (peeked != null) {
            return true;
        }
        return scanner.hasNext();
    }

    /**
     * Ignores whitespaces
     *
     * @return non-whitespace token
     */
    public Token nextToken() {
        if (peeked != null) {
            current = peeked;
            peeked = null;
            return current;
        }
        Token next = scanner.next();
        while (next.isWhiteSpace() && scanner.hasNext()) {
            if (next instanceof NewLineToken) registerNewLine(((NewLineToken) next));
            next = scanner.next();
        }
        current = next;
        return next;
    }

    /**
     * Does not ignore whitespaces
     *
     * @return token
     */
    public Token nextTokenWithWS() {
        if (peeked != null) {
            current = peeked;
            peeked = null;
            return current;
        }
        Token next = scanner.next();
        current = next;
        return next;
    }


    /**
     * Peeks the next token.
     *
     * @return peeked Token
     */
    public Token peekNext() {
        if (peeked != null) {
            return peeked;
        }
        Token next = scanner.next();
        while (next.isWhiteSpace()) {
            if (next instanceof NewLineToken) registerNewLine(((NewLineToken) next));
            next = scanner.next();
        }
        peeked = next;
        return peeked;
    }

    private void registerNewLine(NewLineToken nl) {
        this.newLineOffSets.add(nl.getStartOffset());
    }

    /**
     * Recover to given token
     *
     * @param to Token to which lexer recovers
     */
    public final void recover(Token to) {
        scanner.recover(to);
        peeked = null;
        peekNext();
        current = to;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public List<Integer> getNewLineOffSets() {
        return Collections.unmodifiableList(newLineOffSets);
    }

    public Token getCurrent() {
        return current;
    }
}
