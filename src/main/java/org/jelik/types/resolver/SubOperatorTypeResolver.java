package org.jelik.types.resolver;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.operators.SubExpr;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public class SubOperatorTypeResolver extends BaseOpTypeResolver {

    public Type resolve(SubExpr subExpr, CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            return subExpr.getRight().getGenericReturnType();
        }
        return super.resolve(subExpr, compilationContext);
    }
}
