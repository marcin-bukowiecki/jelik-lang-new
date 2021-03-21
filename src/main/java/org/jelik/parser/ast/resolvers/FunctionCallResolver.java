package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.JelikBuiltinFunctionsRegister;
import org.jelik.compiler.data.ClassDataImpl;
import org.jelik.compiler.data.MethodData;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.functions.FunctionCall;
import org.jelik.parser.ast.functions.TargetFunctionCallProvider;
import org.jelik.types.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionCallResolver {

    /**
     * Tries to find target method for given callNode according to passed argument types.
     * If found method is an varargs call, given portion of passed arguments must be transformed into an array. This array
     * will act as a single argument i.e.:
     *
     * given (1,2,3,"foo","bar")
     *
     * target (int,int,int,String...)
     *
     * will be transformed into:
     *
     * given (1,2,3,new String[] { "foo", "bar"})
     *
     * @param functionCall given function call
     * @param compilationContext {@link CompilationContext}
     * @return found method or null if the method wasn't found
     */
    public Optional<TargetFunctionCallProvider<?>> resolveCall(final FunctionCall functionCall, final CompilationContext compilationContext) {
        var providedArgumentTypes = functionCall.getArgumentTypes();

        if (functionCall.getOwner() == null) {
            final List<? extends MethodData> byName = compilationContext
                    .getCurrentModule()
                    .findByName(functionCall.getName(), compilationContext);
            final List<MethodData> result = new ArrayList<>(byName);
            result.addAll(JelikBuiltinFunctionsRegister.INSTANCE.findByName(functionCall.getName()));

            result.addAll(compilationContext.currentFunction().findSymbol(functionCall.getName(), compilationContext)
                    .map(frs -> frs.findMethodData(functionCall, compilationContext)).orElse(Collections.emptyList()));

            for (ImportDeclaration importDeclaration : compilationContext.getCurrentModule().getModuleDeclaration().getImports()) {
                final Type type = importDeclaration.getType();
                final ClassDataImpl classData = type.findClassData(compilationContext);
                result.addAll(classData.findByName(functionCall.getName(), compilationContext));
            }

            for (ImportDeclaration importDeclaration : compilationContext.getCurrentModule().getModuleDeclaration().getImports()) {
                final Type type = importDeclaration.getType();
                if (type.getName().equals(functionCall.getName())) {
                    result.addAll(type.findClassData(compilationContext).getConstructorScope());
                }
            }

            for (ClassDeclaration aClass : compilationContext.getCurrentModule().getModuleDeclaration().getClasses()) {
                if (aClass.getSimpleName().equals(functionCall.getName())) {
                    result.addAll(aClass.getConstructorDeclarations());
                }
            }

            return resolveCall(compilationContext, providedArgumentTypes, result);
        }

        List<MethodData> builtInFunctions = JelikBuiltinFunctionsRegister.INSTANCE.findByName(functionCall.getName());

        if (builtInFunctions.isEmpty()) {
            var owner = functionCall.getOwner();
            var classData = owner.findClassData(compilationContext);
            var byName = classData.findByName(functionCall.getName(), compilationContext);
            return resolveCall(compilationContext, providedArgumentTypes, byName);
        } else {
            return resolveCall(compilationContext, providedArgumentTypes, builtInFunctions)
                    .<Optional<TargetFunctionCallProvider<?>>>map(Optional::of)
                    .orElseGet(() -> {
                        var owner = functionCall.getOwner();
                        var classData = owner.findClassData(compilationContext);
                        var byName = classData.findByName(functionCall.getName(), compilationContext);
                        return resolveCall(compilationContext, providedArgumentTypes, byName);
            });
        }
    }

    public Optional<TargetFunctionCallProvider<?>> resolveCall(final CompilationContext compilationContext,
                                                                final List<Type> providedArgumentTypes,
                                                                final List<? extends MethodData> possibleMethods) {

        TreeMap<Integer, MethodData> matchingResult = new TreeMap<>();

        outerLoop: for (var methodData : possibleMethods) {
            if (methodData.getParameterTypes().size() != providedArgumentTypes.size()) {
                continue;
            }

            int i=0;
            int score = 0;

            for (Type requiredType : methodData.getParameterTypes()) {
                if (i >= providedArgumentTypes.size()) {
                    continue outerLoop;
                }
                Type providedType = providedArgumentTypes.get(i);
                if (!providedType.isAssignableTo(requiredType, compilationContext)) {
                    continue outerLoop;
                } else {
                    int depth = providedType.getAssignableTypeAndHierarchyDepth(requiredType, compilationContext);
                    score = score + depth; // depth == 0 when types are equal either the depth of class hierarchy
                }
                i++;
            }

            matchingResult.put(score, methodData);
        }

        if (matchingResult.isEmpty()) {
            return Optional.empty();
        }

        //get the first best matching method (lowest score is best)
        return Optional.of(matchingResult.firstEntry().getValue().getCallProvider());
    }
}
