package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapGetExpr;
import org.jelik.parser.ast.arrays.ArrayOrMapSetExpr;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.StoreLocalNode;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.operators.AssignOperator;

/**
 * @author Marcin Bukowiecki
 */
public class LocalVariableAccessSymbolResult implements FindSymbolResult {

    private final LocalVariable localVariable;

    public LocalVariableAccessSymbolResult(LocalVariable localVariable) {
        this.localVariable = localVariable;
    }

    @Override
    public Expression replaceNode(LiteralExpr literalExpr) {
        LiteralToken literalToken = literalExpr.getLiteralToken();

        if (literalExpr.parent instanceof AssignExpr) {
            var isArrayOrMapGet = literalExpr.getFurtherExpression() instanceof ArrayOrMapGetExpr;
            if (isArrayOrMapGet) {
                var newExpr = new ArrayOrMapSetExpr(
                        new GetLocalNode(literalToken, localVariable),
                        ((ArrayOrMapGetExpr) literalExpr.getFurtherExpression()).getLeftBracketToken(),
                        ((ArrayOrMapGetExpr) literalExpr.getFurtherExpression()).getExpression(),
                        ((ArrayOrMapGetExpr) literalExpr.getFurtherExpression()).getRightBracketToken(),
                        ((AssignOperator) ((AssignExpr) literalExpr.parent).getOp()),
                        ((AssignExpr) literalExpr.parent).getRight());
                literalExpr.parent.parent.replaceWith(((AssignExpr) literalExpr.parent), newExpr);
                return newExpr;
            } else {
                var newExpr = new StoreLocalNode(literalToken, localVariable);
                newExpr.parent = literalExpr.parent;
                newExpr.parent.replaceWith(literalExpr, newExpr);
                return newExpr;
            }
        } else {
            var newExpr = new GetLocalNode(literalToken, localVariable);
            newExpr.setFurtherExpression(literalExpr.getFurtherExpression());
            newExpr.parent = literalExpr.parent;
            newExpr.parent.replaceWith(literalExpr, newExpr);
            return newExpr;
        }
    }

    public LocalVariable getLocalVariable() {
        return localVariable;
    }
}
