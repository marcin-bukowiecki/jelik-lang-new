package org.jelik.types;

import org.jelik.compiler.common.TypeEnum;

/**
 * @author Marcin Bukowiecki
 */
public class JVMArrayType extends Type {

    private final Type inner;

    public JVMArrayType(Type inner) {
        super("[" + inner.getName(), "[" + inner.getCanonicalName(), TypeEnum.arrayT);
        this.inner = inner;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public String getDescriptor() {
        if (inner instanceof JVMArrayType) {
            return "[Ljava/lang/Object;";
        } else {
            return "[" + inner.getDescriptor();
        }
    }

    @Override
    public String getInternalName() {
        if (inner instanceof JVMArrayType) {
            return "[Ljava/lang/Object;";
        } else {
            return "[" + inner.getInternalName();
        }
    }

    @Override
    public Type getInnerType(int i) {
        assert  i == 0;
        return inner;
    }

    @Override
    public Type deepGenericCopy() {
        return new JVMArrayType(this.inner.deepGenericCopy());
    }

    @Override
    public String toString() {
        return getCanonicalName();
    }
}
