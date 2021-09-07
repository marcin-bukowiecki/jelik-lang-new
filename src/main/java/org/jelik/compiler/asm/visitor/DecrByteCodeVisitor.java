package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.locals.GetLocalNode;
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
public class DecrByteCodeVisitor extends TypeVisitor {

    private final Expression subject;

    private final MethodVisitorAdapter methodVisitorAdapter;

    public DecrByteCodeVisitor(Expression subject, MethodVisitorAdapter methodVisitorAdapter) {
        this.subject = subject;
        this.methodVisitorAdapter = methodVisitorAdapter;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.intLoad(index);
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
            methodVisitorAdapter.intStore(index);
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.intLoad(index);
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
            methodVisitorAdapter.intStore(index);
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {
        throw new CompileException("Can't apply -- operator to a Boolean type", subject, compilationContext.getCurrentModule());
    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.doubleLoad(index);
            methodVisitorAdapter.visitConstant(JVMDoubleType.INSTANCE, 1.0d);
            methodVisitorAdapter.visitDoubleSub();
            methodVisitorAdapter.doubleStore(index);
        } else {
            methodVisitorAdapter.visitConstant(JVMDoubleType.INSTANCE, 1.0d);
            methodVisitorAdapter.visitDoubleSub();
        }
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.longLoad(index);
            methodVisitorAdapter.visitConstant(JVMLongType.INSTANCE, 1L);
            methodVisitorAdapter.visitLongSub();
            methodVisitorAdapter.longStore(index);
        } else {
            methodVisitorAdapter.visitConstant(JVMLongType.INSTANCE, 1L);
            methodVisitorAdapter.visitLongSub();
        }
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.intLoad(index);
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
            methodVisitorAdapter.intStore(index);
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.intLoad(index);
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
            methodVisitorAdapter.intStore(index);
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntSub();
        }
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.floatLoad(index);
            methodVisitorAdapter.visitConstant(JVMFloatType.INSTANCE, 1L);
            methodVisitorAdapter.visitFloatSub();
            methodVisitorAdapter.floatStore(index);
        } else {
            methodVisitorAdapter.visitConstant(JVMFloatType.INSTANCE, 1L);
            methodVisitorAdapter.visitFloatSub();
        }
    }

    @Override
    public void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext) {
        throw new CompileException("Can't apply ++ operator to a " + type.getCanonicalName() + " type", subject, compilationContext.getCurrentModule());
    }

    @Override
    public void visit(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        throw new CompileException("Can't apply ++ operator to a " + type.getCanonicalName() + " type", subject, compilationContext.getCurrentModule());
    }
}
