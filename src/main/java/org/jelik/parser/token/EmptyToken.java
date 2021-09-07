package org.jelik.parser.token;

/**
 * @author Marcin Bukowiecki
 */
public class EmptyToken extends LiteralToken {

    public static final EmptyToken INSTANCE = new EmptyToken();

    public EmptyToken() {
        super(-1, "", ElementType.empty);
    }
}
