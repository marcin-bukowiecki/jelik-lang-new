package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.types.JVMBooleanType;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMObjectType;
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
public class ByteCodeGetLocalVariableVisitor extends TypeVisitor {

    private final MethodVisitorAdapter methodVisitorAdapter;

    private final GetLocalNode getLocalNode;

    public ByteCodeGetLocalVariableVisitor(GetLocalNode getLocalNode, MethodVisitorAdapter methodVisitorAdapter) {
        this.methodVisitorAdapter = methodVisitorAdapter;
        this.getLocalNode = getLocalNode;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intLoad(index);
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intLoad(index);
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intLoad(index);
    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.doubleLoad(index);
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.longLoad(index);
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intLoad(index);
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intLoad(index);
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.floatLoad(index);
    }

    @Override
    public void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.aload(index);
    }

    @Override
    public void visit(@NotNull IntegerWrapperType type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.aload(index);
    }

    @Override
    public void visit(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        int index = getLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.aload(index);
    }
}
