package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.types.TypeAccessNode;
import org.jelik.parser.ast.types.TypeMappingContext;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class GenericTypeResolver extends AstVisitor {

    @Override
    public void visit(@NotNull FunctionCallExpr functionCallExpr, @NotNull CompilationContext compilationContext) {
        ASTNode caller = functionCallExpr.getCaller();

        if (caller instanceof GetLocalNode) {
            Type genericType = functionCallExpr.getGenericType();

            LocalVariable localVariable = ((GetLocalNode) caller).getLocalVariable();
            Type type = localVariable.getGenericType();
            TypeMappingContext mappings = type.getMappings();

            for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
                Type genericReturnType = argument.getGenericReturnType();
                for (var entry : mappings.typeMap.entrySet()) {
                    genericReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                }

                TypeMappingContext argumentMappings = genericReturnType.getMappings();
                for (var entry : argumentMappings.typeMap.entrySet()) {
                    if (genericType.equals(entry.getKey())) {
                        functionCallExpr.setGenericType(entry.getValue());
                    } else {
                        genericType.setTypeVariable(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else if (caller instanceof TypeAccessNode) {
            Type type = ((TypeAccessNode) caller).getGenericReturnType();
            TypeMappingContext mappings = type.getMappings();

            for (Argument argument : functionCallExpr.getArgumentList().getArguments()) {
                Type genericReturnType = argument.getGenericReturnType();
                for (var entry : mappings.typeMap.entrySet()) {
                    genericReturnType.setTypeVariable(entry.getKey(), entry.getValue());
                }

                break;
            }
        } else if (caller instanceof FunctionCallExpr) {
            var genericType = ((FunctionCallExpr) caller).getGenericType();
            var mappings = genericType.getMappings();

            for (var entry : mappings.typeMap.entrySet()) {
                if (functionCallExpr.getGenericReturnType().equals(entry.getKey())) {
                    functionCallExpr.setGenericType(entry.getValue());
                } else {
                    genericType.setTypeVariable(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
