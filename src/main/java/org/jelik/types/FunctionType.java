package org.jelik.types;

import org.jelik.compiler.common.TypeEnum;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionType extends Type {
    public FunctionType(String name, String canonicalName) {
        super(name, canonicalName, TypeEnum.objectT);
    }

    @Override
    public boolean isFunctionType() {
        return true;
    }
}
