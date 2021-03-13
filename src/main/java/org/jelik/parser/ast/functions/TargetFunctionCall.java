package org.jelik.parser.ast.functions;

import org.jelik.compiler.data.MethodData;
import org.jelik.parser.ast.types.TypeMappingContext;
import org.jelik.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcin Bukowiecki
 */
public class TargetFunctionCall {

    private final MethodData methodData;

    public TargetFunctionCall(MethodData methodData) {
        this.methodData = methodData;
    }

    public MethodData getMethodData() {
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
}
