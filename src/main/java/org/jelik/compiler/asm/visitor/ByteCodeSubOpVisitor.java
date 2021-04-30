package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.parser.ast.operators.SubExpr;
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
 * This visitor generates proper JVM add operation instruction for given type i.e. for int type, this visitor will
 * generate IDIV instruction
 *
 * @author Marcin Bukowiecki
 */
public class ByteCodeSubOpVisitor extends TypeVisitor {

    private final SubExpr subExpr;

    private final MethodVisitorAdapter methodVisitorAdapter;

    public ByteCodeSubOpVisitor(SubExpr subExpr, MethodVisitorAdapter methodVisitorAdapter) {
        this.subExpr = subExpr;
        this.methodVisitorAdapter = methodVisitorAdapter;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            methodVisitorAdapter.visitIntNeg();
        } else {
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            methodVisitorAdapter.visitIntNeg();
        } else {
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {

    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            methodVisitorAdapter.visitDoubleNeg();
        } else {
            methodVisitorAdapter.visitDoubleSub();
        }
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            methodVisitorAdapter.visitLongNeg();
        } else {
            methodVisitorAdapter.visitLongSub();
        }
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            methodVisitorAdapter.visitIntNeg();
        } else {
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            methodVisitorAdapter.visitIntNeg();
        } else {
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        if (subExpr.isNegateExpr()) {
            methodVisitorAdapter.visitFloatNeg();
        } else {
            methodVisitorAdapter.visitFloatSub();
        }
    }

    @Override
    public void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext) {

    }

    @Override
    public void visit(@NotNull Type type, @NotNull CompilationContext compilationContext) {

    }
}
