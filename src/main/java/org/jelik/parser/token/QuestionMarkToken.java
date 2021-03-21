package org.jelik.parser.token;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ParseVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class QuestionMarkToken extends Token {

    public QuestionMarkToken(int row, int col) {
        super("?", row, col, ElementType.questionMark);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitQuestionMark(this, parseContext);
    }
}
