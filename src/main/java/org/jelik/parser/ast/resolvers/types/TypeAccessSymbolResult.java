package org.jelik.parser.ast.resolvers.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.data.MethodData;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.functions.FunctionCall;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.types.TypeAccessNodeTyped;
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
        TypeAccessNodeTyped typeAccessNode = new TypeAccessNodeTyped(literalExpr.getLiteralToken(), type, type.deepGenericCopy());
        literalExpr.getParent().replaceWith(literalExpr, typeAccessNode);
        return typeAccessNode;
    }

    @Override
    public List<? extends MethodData> findMethodData(FunctionCall caller, CompilationContext compilationContext) {
        return type.findMethodData(caller.getName(), compilationContext);
    }

    public Type getType() {
        return type;
    }
}
