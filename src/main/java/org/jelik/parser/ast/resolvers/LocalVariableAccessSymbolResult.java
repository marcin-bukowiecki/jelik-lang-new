package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.data.FunctionReferenceMethodData;
import org.jelik.compiler.data.MethodData;
import org.jelik.compiler.helper.CompilerHelper;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.types.TypeInferenceWalker;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionCall;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.locals.StoreLocalNode;
import org.jelik.parser.ast.operators.AssignExpr;
import org.jelik.parser.token.LiteralToken;
import org.jelik.types.UnresolvedFunctionType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        if (literalExpr.getParent() instanceof AssignExpr) {
            var newExpr = new StoreLocalNode(literalToken, localVariable);
            newExpr.setParent(literalExpr.getParent());
            newExpr.getParent().replaceWith(literalExpr, newExpr);
            return newExpr;
        } else {
            var newExpr = new GetLocalNode(literalToken, localVariable);
            newExpr.setParent(literalExpr.getParent());
            newExpr.getParent().replaceWith(literalExpr, newExpr);
            return newExpr;
        }
    }

    @Override
    public List<? extends MethodData> findMethodData(FunctionCall caller, CompilationContext compilationContext) {
        if (localVariable.isFunctionReference()) {
            var ref = localVariable.getFunctionReference().orElseThrow();
            if (localVariable.getType() instanceof UnresolvedFunctionType) {
                if (!TypeInferenceWalker.INSTANCE.resolveTargetCallForFunctionReference(localVariable,
                        caller,
                        compilationContext)) {

                    CompilerHelper.INSTANCE.raiseTypeCompileError("type.function.unresolvedReference", caller);
                    return Collections.emptyList();
                }
            }
            return ref.getPossibleFunctionsToCall().stream()
                    .map(m -> new FunctionReferenceMethodData(ref,
                            mv -> { mv.aload(localVariable.index); return null; },
                            m.getFunctionType().getFunctionalInterfaceMethod(compilationContext)))
                    .collect(Collectors.toList());
        } else {
            return localVariable.getType().findMethodData(caller.getName(), compilationContext);
        }
    }

    public LocalVariable getLocalVariable() {
        return localVariable;
    }
}
