package org.jelik.parser.ast.resolvers;

import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.LiteralExpr;

public interface FindSymbolResult {
    Expression replaceNode(LiteralExpr literalExpr);
}
