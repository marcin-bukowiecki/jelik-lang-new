package org.jelik.compiler.data;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor;
import org.jelik.parser.ast.functions.FunctionCallExpr;
import org.jelik.parser.ast.functions.providers.BuiltinFunctionCallProvider;
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class JelikBuiltinFunction extends MethodDataImpl {

    private final Function3<FunctionCallExpr, ToByteCodeVisitor, MethodVisitorAdapter, Unit> byteCodeData;

    public JelikBuiltinFunction(int modifiers,
                                @NotNull String name,
                                @NotNull List<Type> parameterTypes,
                                @NotNull List<Type> genericParameterTypes,
                                @NotNull Type returnType,
                                @NotNull Type genericReturnType,
                                @NotNull ClassData owner,
                                @NotNull Function3<FunctionCallExpr,
                                        ToByteCodeVisitor,
                                        MethodVisitorAdapter,
                                        Unit> byteCodeData) {
        super(modifiers, name, parameterTypes, genericParameterTypes, returnType, genericReturnType, owner);
        this.byteCodeData = byteCodeData;
    }

    public Function3<FunctionCallExpr, ToByteCodeVisitor, MethodVisitorAdapter, Unit> getByteCodeData() {
        return byteCodeData;
    }

    @Override
    public TargetFunctionCallProvider<?> getCallProvider() {
        return new BuiltinFunctionCallProvider(this);
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }
}
