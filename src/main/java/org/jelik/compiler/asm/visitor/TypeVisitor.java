package org.jelik.compiler.asm.visitor;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.types.JVMBooleanType;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMNullType;
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
public abstract class TypeVisitor {

    public abstract void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext);

    public abstract void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext);

    public void visit(@NotNull JVMVoidType type, @NotNull CompilationContext compilationContext) {
        this.visit((Type) type, compilationContext);
    }

    public abstract void visit(@NotNull Type type, @NotNull CompilationContext compilationContext);

    public void visit(@NotNull JVMNullType type, @NotNull CompilationContext compilationContext) {
        this.visit((Type) type, compilationContext);
    }

    public void visit(@NotNull IntegerWrapperType type, @NotNull CompilationContext compilationContext) {
        this.visit((Type) type, compilationContext);
    }
}
