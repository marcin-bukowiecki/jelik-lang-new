package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.arguments.ArgumentList;
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionReferenceCallExpr extends FunctionCallExpr {

    public FunctionReferenceCallExpr(final LiteralExpr literalExpr,
                                     final ArgumentList argumentList,
                                     final TargetFunctionCallProvider targetFunctionCall,
                                     final Type owner) {
        super(literalExpr, argumentList);
        this.owner = owner;
        this.targetFunctionCall = targetFunctionCall;
    }
}
