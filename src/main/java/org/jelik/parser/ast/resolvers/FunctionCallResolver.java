package org.jelik.parser.ast.resolvers;

import com.google.common.collect.ImmutableList;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.data.MethodData;
import org.jelik.compiler.functions.ExtensionFunctionRegister;
import org.jelik.compiler.functions.JelikBuiltinFunctionsRegister;
import org.jelik.compiler.helper.CompilerHelper;
import org.jelik.parser.ast.functions.FunctionCall;
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider;
import org.jelik.types.Type;
import org.jelik.types.helpers.TypeHierarchyHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public Optional<? extends TargetFunctionCallProvider<?>> resolveCall(final FunctionCall functionCall,
                                                                         final CompilationContext compilationContext) {
        var providedArgumentTypes = functionCall.getArgumentTypes();

        if (functionCall.getOwner() == null) { //is static call
            var byName = compilationContext
                    .getCurrentModule()
                    .findByName(functionCall.getName(), compilationContext);
            final List<MethodData> variants = new ArrayList<>(byName);
            variants.addAll(JelikBuiltinFunctionsRegister.INSTANCE.findByName(functionCall.getName()));
            variants.addAll(compilationContext.currentFunction().findSymbol(functionCall.getName(), compilationContext)
                    .map(frs -> frs.findMethodData(functionCall, compilationContext)).orElse(Collections.emptyList()));

            for (var importDeclaration : compilationContext
                    .getCurrentModule()
                    .getModuleDeclaration()
                    .getImports()) {
                var type = importDeclaration.getType();
                var classData = type.findClassData(compilationContext);
                variants.addAll(classData.findByName(functionCall.getName(), compilationContext));
            }

            for (var importDeclaration : compilationContext
                    .getCurrentModule()
                    .getModuleDeclaration()
                    .getImports()) {
                var type = importDeclaration.getType();
                if (type.getName().equals(functionCall.getName())) {

                    if (type.isInterface()) {
                        CompilerHelper.INSTANCE.raiseTypeCompileError("type.interface.init", functionCall);
                        return Optional.empty();
                    }

                    if (type.findClassData(compilationContext).isAbstract()) {
                        compilationContext
                                .getProblemHolder()
                                .reportProblem("access.abstractTypeInit", functionCall);
                    }

                    variants.addAll(type.findClassData(compilationContext).getConstructorScope());
                }
            }

            for (var aClass : compilationContext.getCurrentModule().getModuleDeclaration().getClasses()) {
                if (aClass.getSimpleName().equals(functionCall.getName())) {
                    variants.addAll(aClass.getConstructorDeclarations());
                }
            }

            return resolveCall(compilationContext, providedArgumentTypes, variants);

        } else { //instance call
            var owner = functionCall.getOwner();
            var classData = owner.findClassData(compilationContext);
            var byName = classData.findByName(functionCall.getName(), compilationContext);
            var extensionFunctions = ExtensionFunctionRegister.INSTANCE.findForType(owner, functionCall);
            var variants = new ImmutableList.Builder<MethodData>()
                    .addAll(byName)
                    .addAll(extensionFunctions)
                    .build();
            return resolveCall(compilationContext, providedArgumentTypes, variants);
        }
    }

    public Optional<? extends TargetFunctionCallProvider<?>> resolveCall(final CompilationContext compilationContext,
                                                                final List<Type> providedArgumentTypes,
                                                                final List<? extends MethodData> possibleMethods) {

        TreeMap<Integer /* score */, List<MethodData>> matchingResult = new TreeMap<>();

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

            if (matchingResult.containsKey(score)) {
                matchingResult.get(score).add(methodData);
            } else {
                final ArrayList<MethodData> methods = new ArrayList<>();
                methods.add(methodData);
                matchingResult.put(score, methods);
            }
        }

        if (matchingResult.isEmpty()) {
            return Optional.empty();
        }

        //get the first best matching method (lowest score is best)
        //sort by owner depth
        return Optional.of(matchingResult.firstEntry())
                .map(Map.Entry::getValue)
                .map(l -> {
                    l.sort((o1, o2) -> TypeHierarchyHelper.INSTANCE.getTypeHierarchyScore(o1.getOwner(),
                            o2.getOwner(),
                            compilationContext));
                    return Optional.of(l.get(0).getCallProvider());
                }).orElse(Optional.empty());
    }
}
