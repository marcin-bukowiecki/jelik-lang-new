package org.jelik.types.resolver;

import org.jelik.CompilationContext;
import org.jelik.compiler.exceptions.TypeCompileException;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.CodeEvent;
import org.jelik.parser.ast.operators.AddExpr;
import org.jelik.parser.ast.resolvers.CastToVisitor;
import org.jelik.parser.ast.strings.StringBuilderAppend;
import org.jelik.parser.ast.strings.StringBuilderInit;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMStringType;
import org.jelik.types.Type;
import org.jelik.types.jvm.JVMDoubleType;
import org.jelik.types.jvm.JVMFloatType;
import org.jelik.types.jvm.JVMLongType;

/**
 * @author Marcin Bukowiecki
 */
public class AddOperatorTypeResolver {

    public static Type resolve(AddExpr addExpr, CompilationContext compilationContext) {
        Type leftType = addExpr.getLeft().getGenericReturnType();
        Type rightType = addExpr.getRight().getGenericReturnType();
        FunctionDeclaration functionDeclaration = compilationContext.currentFunction();

        switch (leftType.getTypeEnum()) {
            case int32:
                switch (rightType.getTypeEnum()) {
                    case int32:
                        return JVMIntType.INSTANCE;
                    case string:
                        setupStringBuilder(functionDeclaration, addExpr, compilationContext);
                        return JVMStringType.INSTANCE;
                    case int32Wrapper:
                        rightType.visit(new CastToVisitor(addExpr.getRight(), JVMIntType.INSTANCE), compilationContext);
                        return JVMIntType.INSTANCE;
                    default:
                        throw createException(leftType, rightType, addExpr, compilationContext);
                }
            case int64:
                switch (rightType.getTypeEnum()) {
                    case int32:
                    case int64:
                        return JVMLongType.INSTANCE;
                    case string:
                        setupStringBuilder(functionDeclaration, addExpr, compilationContext);
                        return JVMStringType.INSTANCE;
                    default:
                        throw createException(leftType, rightType, addExpr, compilationContext);
                }
            case float32:
                switch (rightType.getTypeEnum()) {
                    case float32:
                        return JVMFloatType.INSTANCE;
                    default:
                        throw createException(leftType, rightType, addExpr, compilationContext);
                }
            case float64:
                switch (rightType.getTypeEnum()) {
                    case float64:
                        return JVMDoubleType.INSTANCE;
                    default:
                        throw createException(leftType, rightType, addExpr, compilationContext);
                }
            case string:
                setupStringBuilder(functionDeclaration, addExpr, compilationContext);
                switch (rightType.getTypeEnum()) {
                    case string:
                        return JVMStringType.INSTANCE;
                }
            default:
                throw createException(leftType, rightType, addExpr, compilationContext);
        }
    }

    public static void setupStringBuilder(final FunctionDeclaration functionDeclaration, final AddExpr addExpr, final CompilationContext compilationContext) {
        boolean stringBuilderInitialized = functionDeclaration.getFunctionContext()
                .isStringBuilderInitialized();

        if (!stringBuilderInitialized) {
            StringBuilderInit stringBuilderInit = new StringBuilderInit();
            addExpr.parent.replaceWith(addExpr, stringBuilderInit);
            stringBuilderInit.setFurtherExpression(new StringBuilderAppend(addExpr.getLeft(), compilationContext));
            stringBuilderInit.getFurtherExpression().setFurtherExpression(new StringBuilderAppend(addExpr.getRight(), compilationContext));
            functionDeclaration.getFunctionContext().addCodeEvent(CodeEvent.stringBuilderInitialized);
        }
    }

    private static RuntimeException createException(Type leftType, Type rightType, AddExpr addExpr, CompilationContext compilationContext) {
        return new TypeCompileException("Operator '+' can't be applied to " +
                leftType.forErrorMessage() +
                " and " +
                rightType.forErrorMessage(),
                addExpr,
                compilationContext.getCurrentModule());
    }
}
