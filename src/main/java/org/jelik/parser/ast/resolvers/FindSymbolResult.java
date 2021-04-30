package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.data.MethodData;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.functions.FunctionCall;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface FindSymbolResult {

    Expression replaceNode(LiteralExpr literalExpr);

    List<? extends MethodData> findMethodData(FunctionCall caller, CompilationContext compilationContext);
}
