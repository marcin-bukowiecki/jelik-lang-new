package org.jelik.types.resolver;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.exceptions.TypeCompileException;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.CodeEvent;
import org.jelik.parser.ast.numbers.Int32ToFloat64Node;
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

        switch (leftType.getTypeEnum()) {
            case int32:
                switch (rightType.getTypeEnum()) {
                    case int32:
                        return JVMIntType.INSTANCE;
                    case string:
                        setupStringBuilder(addExpr, compilationContext);
                        return JVMStringType.INSTANCE;
                    case int32Wrapper:
                        rightType.accept(new CastToVisitor(addExpr.getRight(), JVMIntType.INSTANCE), compilationContext);
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
                        setupStringBuilder(addExpr, compilationContext);
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
                    case int32:
                        addExpr.getRight().getParent().replaceWith(addExpr.getRight(), new Int32ToFloat64Node(addExpr.getRight()));
                    case float64:
                        return JVMDoubleType.INSTANCE;
                    default:
                        throw createException(leftType, rightType, addExpr, compilationContext);
                }
            case string:
                switch (rightType.getTypeEnum()) {
                    case string:
                        AddOperatorTypeResolver.setupStringBuilder(addExpr, compilationContext);
                        return JVMStringType.INSTANCE;
                }
            default:
                switch (rightType.getTypeEnum()) {
                    case string:
                        AddOperatorTypeResolver.setupStringBuilder(addExpr, compilationContext);
                        return JVMStringType.INSTANCE;
                }
                throw createException(leftType, rightType, addExpr, compilationContext);
        }
    }

    public static void setupStringBuilder(final AddExpr addExpr, final CompilationContext compilationContext) {
        StringBuilderInit stringBuilderInit = new StringBuilderInit();
        addExpr.getParent().replaceWith(addExpr, stringBuilderInit);
        final StringBuilderAppend stringBuilderAppend = new StringBuilderAppend(addExpr.getLeft(), compilationContext);
        stringBuilderInit.setFurtherExpression(stringBuilderAppend);
        stringBuilderAppend.setFurtherExpression(new StringBuilderAppend(addExpr.getRight(), compilationContext));
    }

    public static void setupStringBuilder(final FunctionDeclaration functionDeclaration,
                                          final AddExpr addExpr,
                                          final CompilationContext compilationContext) {

        final boolean stringBuilderInitialized = functionDeclaration.getFunctionContext()
                .isStringBuilderInitialized();

        if (!stringBuilderInitialized) {
            StringBuilderInit stringBuilderInit = new StringBuilderInit();
            addExpr.getParent().replaceWith(addExpr, stringBuilderInit);
            StringBuilderAppend stringBuilderAppend = new StringBuilderAppend(addExpr.getLeft(), compilationContext);
            stringBuilderInit.setFurtherExpression(stringBuilderAppend);
            functionDeclaration.getFunctionContext().addCodeEvent(CodeEvent.stringBuilderInitialized);
            stringBuilderAppend.setFurtherExpression(addExpr.getRight());
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
