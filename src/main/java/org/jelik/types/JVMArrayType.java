package org.jelik.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.parser.ast.types.JelikGenericType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * @author Marcin Bukowiecki
 */
public class JVMArrayType extends Type {

    private final Type inner;

    public JVMArrayType(Type inner) {
        super("[" + inner.getName(),
                "[" + inner.getCanonicalName(),
                TypeEnum.arrayT,
                Collections.singletonList(new JelikGenericType("_Dummy_")),
                Collections.singletonList(inner)
        );
        this.inner = inner;
    }

    public Type getElementType() {
        return inner;
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
            return "[L" + inner.getInternalName() + ";";
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

    @Override
    public boolean isAssignableTo(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        if (type instanceof JVMArrayType) {
            if (this.inner.isAssignableTo(((JVMArrayType) type).inner, compilationContext)) {
                return true;
            }
        }
        return super.isAssignableTo(type, compilationContext);
    }
}
