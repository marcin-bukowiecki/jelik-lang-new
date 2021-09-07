package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.ReferenceExpressionImpl;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.types.TypeAccessNodeTyped;
import org.jelik.parser.ast.types.TypeMappingContext;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class GenericTypeResolver extends AstVisitor {

    @Override
    public void visitFunctionCall(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        var caller = functionCallExpr.getCaller();

        if (caller instanceof GetLocalNode) {
            Type functionCallReturnType = functionCallExpr.getGenericType();
            LocalVariable localVariable = ((GetLocalNode) caller).getLocalVariable();
            Type type = localVariable.getGenericType();
            TypeMappingContext mappings = type.getMappings();

            for (var entry : mappings.typeMap.entrySet()) {
                if (functionCallReturnType.equals(entry.getKey())) {
                    functionCallExpr.setGenericType(entry.getValue());
                } else {
                    functionCallReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                }
            }

            for (var argument : functionCallExpr.getArgumentList().getArguments()) {
                Type genericReturnType = argument.getGenericReturnType();
                for (var entry : mappings.typeMap.entrySet()) {
                    genericReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                }

                TypeMappingContext argumentMappings = genericReturnType.getMappings();
                for (var entry : argumentMappings.typeMap.entrySet()) {
                    if (functionCallReturnType.equals(entry.getKey())) {
                        functionCallExpr.setGenericType(entry.getValue());
                    } else {
                        functionCallReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else if (caller instanceof TypeAccessNodeTyped) {
            Type type = ((TypeAccessNodeTyped) caller).getGenericReturnType();
            TypeMappingContext mappings = type.getMappings();

            for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
                Type genericReturnType = argument.getGenericReturnType();
                for (var entry : mappings.typeMap.entrySet()) {
                    genericReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                }

                break;
            }
        } else if (caller instanceof ReferenceExpressionImpl) {
            var genericType = ((ReferenceExpressionImpl) caller).getGenericReturnType();
            var mappings = genericType.getMappings();

            for (var entry : mappings.typeMap.entrySet()) {
                if (functionCallExpr.getGenericType().equals(entry.getKey())) {
                    functionCallExpr.setGenericType(entry.getValue());
                } else {
                    genericType.setTypeVariable(entry.getKey(), entry.getValue());
                }
            }

            Type functionCallReturnType = functionCallExpr.getGenericType();
            for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
                Type genericReturnType = argument.getGenericReturnType();
                for (var entry : mappings.typeMap.entrySet()) {
                    genericReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                }

                TypeMappingContext argumentMappings = genericReturnType.getMappings();
                for (var entry : argumentMappings.typeMap.entrySet()) {
                    if (functionCallReturnType.equals(entry.getKey())) {
                        functionCallExpr.setGenericType(entry.getValue());
                    } else {
                        functionCallReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        getMappingForFunctionCall(functionCallExpr);
    }

    public static void getMappingForFunctionCall(FunctionCallExpr functionCallExpr) {
        final Type genericType = functionCallExpr.getGenericType();

        final List<Type> expectedTypeParameters = functionCallExpr
                .getTargetFunctionCallProvider()
                .getMethodData()
                .getExpectedTypeParameters();

        final List<TypeNode> givenTypes = functionCallExpr
                .getLiteralExpr()
                .getTypeParameterListNode()
                .getTypes();

        int i = 0;
        for (TypeNode givenType : givenTypes) {
            final Type type = expectedTypeParameters.get(i);
            if (type.equals(genericType)) {
                functionCallExpr.setGenericType(givenType.getGenericType().deepGenericCopy());
            }
            i++;
        }

        if (functionCallExpr.getTargetFunctionCallProvider().isConstructor()) {
            final Type owner = functionCallExpr.getOwner().deepGenericCopy();
            i = 0;
            for (TypeNode givenType : givenTypes) {
                final Type type = expectedTypeParameters.get(i);
                owner.setTypeVariable(type, givenType.getType());
                i++;
            }
            functionCallExpr.setGenericType(owner);
        }
    }
}
