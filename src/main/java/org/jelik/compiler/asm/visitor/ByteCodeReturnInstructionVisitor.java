package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
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

/**
 * @author Marcin Bukowiecki
 */
public class ByteCodeReturnInstructionVisitor extends TypeVisitor {

    private final MethodVisitorAdapter methodVisitorAdapter;

    public ByteCodeReturnInstructionVisitor(MethodVisitorAdapter methodVisitorAdapter) {
        this.methodVisitorAdapter = methodVisitorAdapter;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitIReturn();
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitIReturn();
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitIReturn();
    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitDReturn();
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitLReturn();
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitIReturn();
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitIReturn();
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitFReturn();
    }

    @Override
    public void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitAReturn();
    }

    @Override
    public void visit(@NotNull JVMVoidType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitReturn();
    }

    @Override
    public void visit(@NotNull IntegerWrapperType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitAReturn();
    }

    @Override
    public void visit(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.visitAReturn();
    }
}
