package org.jelik.parser.ast.strings;

import com.google.common.collect.Lists;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.token.ApostropheToken;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author Marcin Bukowiecki
 */
public class StringParser implements TokenVisitor<StringExpression> {

    private final ApostropheToken apostropheToken;

    public StringParser(ApostropheToken apostropheToken) {
        this.apostropheToken = apostropheToken;
    }

    @Override
    public @NotNull StringExpression visit(@NotNull ParseContext parseContext) {
        ArrayList<Token> tokens = Lists.newArrayList();

        Lexer lexer = parseContext.getLexer();

        while (lexer.hasNextToken()) {
            Token token = lexer.nextTokenWithWS();
            if (token.getTokenType() == ElementType.apostrophe) {
                break;
            }
            tokens.add(token);
        }

        return new StringExpression(apostropheToken, tokens, ((ApostropheToken) lexer.getCurrent()));
    }
}
