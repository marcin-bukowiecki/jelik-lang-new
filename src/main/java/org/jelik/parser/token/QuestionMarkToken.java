package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class QuestionMarkToken extends Token {

    public QuestionMarkToken(int offset) {
        super("?", offset, ElementType.questionMark);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitQuestionMark(this, parseContext);
    }
}
