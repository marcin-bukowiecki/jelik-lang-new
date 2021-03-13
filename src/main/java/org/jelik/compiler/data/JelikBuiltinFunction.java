package org.jelik.compiler.data;

import lombok.Getter;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Marcin Bukowiecki
 */
public class JelikBuiltinFunction extends MethodDataImpl {

    @Getter
    private final Consumer<MethodVisitorAdapter> byteCodeData;

    public JelikBuiltinFunction(int modifiers,
                                @NotNull String name,
                                @NotNull List<Type> parameterTypes,
                                @NotNull List<Type> genericParameterTypes,
                                @NotNull Type returnType,
                                @NotNull Type genericReturnType,
                                @NotNull ClassData owner,
                                Consumer<MethodVisitorAdapter> byteCodeData) {
        super(modifiers, name, parameterTypes, genericParameterTypes, returnType, genericReturnType, owner);
        this.byteCodeData = byteCodeData;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }
}
