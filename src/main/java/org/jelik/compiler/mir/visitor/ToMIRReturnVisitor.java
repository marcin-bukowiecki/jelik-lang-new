package org.jelik.compiler.mir.visitor;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.mir.MIRValue;
import org.jelik.compiler.mir.ret.MIRDoubleReturn;
import org.jelik.compiler.mir.ret.MIRFloatReturn;
import org.jelik.compiler.mir.ret.MIRIntReturn;
import org.jelik.compiler.mir.ret.MIRLongReturn;
import org.jelik.compiler.mir.ret.MIRRefReturn;
import org.jelik.compiler.mir.ret.MIRVoidReturn;
import org.jelik.types.JVMBooleanType;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMObjectType;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jelik.types.jvm.JVMByteType;
import org.jelik.types.jvm.JVMCharType;
import org.jelik.types.jvm.JVMDoubleType;
import org.jelik.types.jvm.JVMFloatType;
import org.jelik.types.jvm.JVMLongType;
import org.jelik.types.jvm.JVMShortType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Marcin Bukowiecki
 */
public class ToMIRReturnVisitor extends TypeVisitor {

    private MIRValue returnStatement;

    private final MIRValue value;

    public ToMIRReturnVisitor(@NotNull MIRValue value) {
        this.value = value;
    }

    @Nullable
    public MIRValue getReturnStatement() {
        return returnStatement;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRIntReturn(value);
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRIntReturn(value);
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRIntReturn(value);
    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRDoubleReturn(value);
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRLongReturn(value);
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRIntReturn(value);
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRIntReturn(value);
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRFloatReturn(value);
    }

    @Override
    public void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRRefReturn(value);
    }

    @Override
    public void visit(@NotNull JVMVoidType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRVoidReturn();
    }

    @Override
    public void visit(@NotNull IntegerWrapperType type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRRefReturn(value);
    }

    @Override
    public void visit(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        returnStatement = new MIRRefReturn(value);
    }
}
