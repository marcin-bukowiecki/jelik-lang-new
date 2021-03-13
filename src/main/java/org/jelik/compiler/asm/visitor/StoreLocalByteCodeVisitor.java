package org.jelik.compiler.asm.visitor;

import org.jelik.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.parser.ast.locals.WithLocalVariable;
import org.jelik.types.JVMBooleanType;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMObjectType;
import org.jelik.types.Type;
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
public class StoreLocalByteCodeVisitor extends TypeVisitor {

    private final WithLocalVariable storeLocalNode;

    private final MethodVisitorAdapter methodVisitorAdapter;

    public StoreLocalByteCodeVisitor(WithLocalVariable storeLocalNode, MethodVisitorAdapter methodVisitorAdapter) {
        this.storeLocalNode = storeLocalNode;
        this.methodVisitorAdapter = methodVisitorAdapter;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intStore(index);
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intStore(index);
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intStore(index);
    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.doubleStore(index);
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.longStore(index);
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intStore(index);
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.intStore(index);
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.floatStore(index);
    }

    @Override
    public void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.objectStore(index);
    }

    @Override
    public void visit(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        int index = storeLocalNode.getLocalVariable().getIndex();
        methodVisitorAdapter.objectStore(index);
    }
}
