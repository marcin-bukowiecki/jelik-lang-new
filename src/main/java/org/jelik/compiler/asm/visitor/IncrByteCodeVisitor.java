package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.MethodVisitorAdapter;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.parser.ast.ConsumingExpression;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.locals.GetLocalNode;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.IncrExpr;
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

import java.util.Collections;

/**
 * @author Marcin Bukowiecki
 */
public class IncrByteCodeVisitor extends TypeVisitor {

    private final IncrExpr incrExpr;

    private final Expression subject;

    private final MethodVisitorAdapter methodVisitorAdapter;

    public IncrByteCodeVisitor(IncrExpr incrExpr, Expression subject, MethodVisitorAdapter methodVisitorAdapter) {
        this.subject = subject;
        this.incrExpr = incrExpr;
        this.methodVisitorAdapter = methodVisitorAdapter;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.incr(index, 1);
            if (incrExpr.getParent() instanceof ConsumingExpression) {
                methodVisitorAdapter.intLoad(index);
            }
            if (subject.getParent() instanceof ConsumingExpression || subject.getParent() instanceof AbstractOpExpr) {
                methodVisitorAdapter.intLoad(index);
            }
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntAdd();
        }
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.incr(index, 1);
            if (incrExpr.getParent() instanceof ConsumingExpression) {
                methodVisitorAdapter.intLoad(index);
            }
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntAdd();
        }
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {
        throw new CompileException("Can't apply ++ operator to a Boolean type", subject, compilationContext.getCurrentModule());
    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.doubleLoad(index);
            methodVisitorAdapter.visitConstant(JVMDoubleType.INSTANCE, 1.0d);
            methodVisitorAdapter.visitDoubleAdd();
            if (!(incrExpr.getParent() instanceof ConsumingExpression)) {
                methodVisitorAdapter.doubleStore(index);
            }
        } else {
            methodVisitorAdapter.visitConstant(JVMDoubleType.INSTANCE, 1.0d);
            methodVisitorAdapter.visitDoubleAdd();
        }
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.visitConstant(JVMLongType.INSTANCE, 1L);
            methodVisitorAdapter.visitLongAdd();
            if (incrExpr.getParent() instanceof ConsumingExpression) {
                methodVisitorAdapter.dup2();
            }
            methodVisitorAdapter.longStore(index);
        } else {
            methodVisitorAdapter.visitConstant(JVMLongType.INSTANCE, 1L);
            methodVisitorAdapter.visitLongAdd();
        }
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.incr(index, 1);
            if (incrExpr.getParent() instanceof ConsumingExpression) {
                methodVisitorAdapter.intLoad(index);
            }
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntAdd();
        }
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.incr(index, 1);
            if (incrExpr.getParent() instanceof ConsumingExpression) {
                methodVisitorAdapter.intLoad(index);
            }
        } else {
            methodVisitorAdapter.pushInt(1);
            methodVisitorAdapter.visitIntAdd();
        }
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.floatLoad(index);
            methodVisitorAdapter.visitConstant(JVMFloatType.INSTANCE, 1.0f);
            methodVisitorAdapter.visitFloatAdd();
            if (!(incrExpr.getParent() instanceof ConsumingExpression)) {
                methodVisitorAdapter.floatStore(index);
            }
        } else {
            methodVisitorAdapter.visitConstant(JVMFloatType.INSTANCE, 1.0f);
            methodVisitorAdapter.visitFloatAdd();
        }
    }

    @Override
    public void visit(@NotNull IntegerWrapperType type, @NotNull CompilationContext compilationContext) {
        methodVisitorAdapter.invokeInstance("java/lang/Integer", "intValue", "()I", JVMIntType.INSTANCE, Collections.emptyList(), false);
        methodVisitorAdapter.pushInt(1);
        methodVisitorAdapter.visitIntAdd();
        if (subject instanceof GetLocalNode) {
            int index = ((GetLocalNode) subject).getLocalVariable().getIndex();
            methodVisitorAdapter.invokeStatic("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",
                    IntegerWrapperType.INSTANCE,
                    Collections.singletonList(JVMIntType.INSTANCE),
                    false);
            if (!(incrExpr.getParent() instanceof ConsumingExpression)) {
                methodVisitorAdapter.objectStore(index);
            }
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
