package org.jelik.parser.ast.resolvers;

import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.types.TypeAccessNode;
import org.jelik.types.Type;

public class TypeAccessSymbolResult implements FindSymbolResult {

    private final Type type;

    public TypeAccessSymbolResult(Type type) {
        this.type = type;
    }

    @Override
    public Expression replaceNode(LiteralExpr literalExpr) {
        TypeAccessNode typeAccessNode = new TypeAccessNode(literalExpr.getLiteralToken(), type, type.deepGenericCopy());
        literalExpr.parent.replaceWith(literalExpr, typeAccessNode);
        typeAccessNode.setFurtherExpression(literalExpr.getFurtherExpression());
        return typeAccessNode;

        /*
        return literalExpr.getFurtherExpressionOpt().map(expr -> {
            if (expr instanceof DotCallExpr) {
                TypeAccessNode typeAccessNode = new TypeAccessNode(literalExpr.getLiteralToken(), type, type.deepGenericCopy());
                typeAccessNode.setFurtherExpression(expr);
                literalExpr.parent.replaceWith(literalExpr, typeAccessNode);
                return typeAccessNode;
            } else {
                return null;
            }
        }).orElse(null);*/
    }

    public Type getType() {
        return type;
    }
}
