package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.parser.ast.Expression;
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
public class CastToVisitor extends TypeVisitor {

    private final Expression from;

    private final Type target;

    public CastToVisitor(Expression from, Type target) {
        this.from = from;
        this.target = target;
    }

    @Override
    public void visit(@NotNull JVMIntType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMCharType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMBooleanType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMDoubleType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMLongType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMShortType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMByteType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMFloatType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull JVMObjectType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull Type type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }

    @Override
    public void visit(@NotNull IntegerWrapperType type, @NotNull CompilationContext compilationContext) {
        target.castFrom(from, type, compilationContext);
    }
}
