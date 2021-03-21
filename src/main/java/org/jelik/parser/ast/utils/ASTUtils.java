package org.jelik.parser.ast.utils;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.token.Token;

/**
 * @author Marcin Bukowiecki
 */
public class ASTUtils {

    public static void checkNewLine(Expression currentExpr, Token currentToken, ParseContext parseContext) {
        if (currentExpr.getEndRow() == currentToken.getRow()) {
            throw new SyntaxException("Expected new line before: '" + currentToken.getText() + "'",
                    currentToken,
                    parseContext.getCurrentFilePath());
        }
    }

    public static ASTNode getNonDotCallExpr(DotCallExpr dotCallExpr) {
        if (dotCallExpr.getSubject() instanceof DotCallExpr) {
            return getNonDotCallExpr(((DotCallExpr) dotCallExpr.getSubject()));
        } else {
            return dotCallExpr.getSubject();
        }
    }
}
