package org.jelik.parser.ast.functions;

import kotlin.jvm.functions.Function4;
import org.jelik.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor;
import org.jelik.compiler.data.MethodData;
import org.jelik.parser.ast.types.TypeMappingContext;
import org.jelik.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcin Bukowiecki
 */
public abstract class TargetFunctionCallProvider<T extends MethodData> {

    private final T methodData;

    public TargetFunctionCallProvider(T methodData) {
        this.methodData = methodData;
    }

    public T getMethodData() {
        return methodData;
    }

    public boolean isConstructor() {
        return methodData.getName().equals("<init>");
    }

    public TypeMappingContext getParameterTypeMappings() {
        Map<Type, Type> result = new HashMap<>();

        int i = 0;
        for (Type typeParameter : methodData.getParameterTypes()) {
            result.put(typeParameter, methodData.getGenericParameterTypes().get(i));
            i++;
        }

        return new TypeMappingContext(result);
    }

    public void resolveCallback(FunctionCallExpr functionCallExpr, CompilationContext compilationContext) {

    }

    public abstract Function4<MethodVisitorAdapter,
                ToByteCodeVisitor,
                FunctionCallExpr,
                CompilationContext,
                Boolean> getCodeGenProvider();
}
