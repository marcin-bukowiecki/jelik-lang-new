package org.jelik.parser.ast.functions.providers;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor;
import org.jelik.compiler.data.MethodData;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.types.TypeMappingContext;
import org.jelik.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for function call provider
 *
 * @author Marcin Bukowiecki
 */
public abstract class TargetFunctionCallProvider<T extends MethodData> {

    protected T methodData;

    public TargetFunctionCallProvider(T methodData) {
        this.methodData = methodData;
    }

    public T getMethodData() {
        return methodData;
    }

    public boolean isConstructor() {
        return methodData.getName().equals("<init>");
    }

    @Deprecated
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

    public abstract Function3<FunctionCallExpr, ToByteCodeVisitor, MethodVisitorAdapter, Unit> getCodeGenProvider();

    public boolean isExt() {
        return false;
    }
}
