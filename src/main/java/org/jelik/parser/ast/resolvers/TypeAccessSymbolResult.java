package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.data.MethodData;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.functions.FunctionCall;
import org.jelik.parser.ast.types.TypeAccessNode;
import org.jelik.types.Type;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
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
    }

    @Override
    public List<MethodData> findMethodData(FunctionCall caller, CompilationContext compilationContext) {
        return type.findMethodData(caller.getName(), compilationContext);
    }

    public Type getType() {
        return type;
    }
}
