package org.jelik.parser.ast.types;

import org.jelik.types.Type;

import java.util.Map;

public class TypeMappingContext {

    public final Map<Type, Type> typeMap;

    public TypeMappingContext(Map<Type, Type> typeMap) {
        this.typeMap = typeMap;
    }

    public void setSymbols(TypeMappingContext with) {
        for (Map.Entry<Type, Type> entry : with.typeMap.entrySet()) {
            Type key = entry.getKey();
            Type value = entry.getValue();

            for (Map.Entry<Type, Type> thisEntry : typeMap.entrySet()) {
                if (thisEntry.getKey().equals(key)) {
                    thisEntry.setValue(value.deepGenericCopy());
                } else {
                    thisEntry.getValue().setTypeVariable(key, value);
                }
            }
        }
    }

    public void setSymbol(TypeMappingContext with) {

    }

    @Override
    public String toString() {
        var acc = new StringBuilder();

        for (Map.Entry<Type, Type> entry : typeMap.entrySet()) {
            acc.append(entry.getKey()).append(" -> ").append(entry.getValue()).append(", ");
        }

        return acc.toString();
    }
}
