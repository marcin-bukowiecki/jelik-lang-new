package org.jelik.types;

import com.google.common.collect.Sets;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.JelikCompiler;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.common.TypeEnum;
import org.jelik.compiler.data.ClassData;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.numbers.Float32ToInt32Node;
import org.jelik.parser.ast.numbers.IntegerWrapperToInt32Node;
import org.jelik.parser.ast.numbers.ObjectToInt32Node;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jelik.types.jvm.JVMFloatType;
import org.jelik.types.jvm.NumberType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

/**
 * @author Marcin Bukowiecki
 */
public class JVMIntType extends NumberType {

    public static final JVMIntType INSTANCE = new JVMIntType();

    public JVMIntType() {
        super("Int", "Int", TypeEnum.int32);
    }

    public JVMIntType(String name, String canonicalName, TypeEnum t) {
        super(name, canonicalName, t);
    }

    @Override
    public ClassData findClassData(@NotNull CompilationContext compilationContext) {
        return JelikCompiler.INSTANCE.createJavaClassData(int.class, compilationContext);
    }

    @Override
    protected boolean isNumberAssignableTo(Type type, CompilationContext compilationContext) {
        Objects.requireNonNull(type);
        if (type.getTypeEnum() == null) {
            return false;
        }
        switch (type.getTypeEnum()) {
            case int64:
            case int64Wrapper:
            case int32:
            case int32Wrapper:
            case float64:
            case float64Wrapper:
            case float32:
            case float32Wrapper:
            case objectT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public NumberType getWrapperType() {
        return IntegerWrapperType.INSTANCE;
    }

    @Override
    public void accept(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    @Override
    public String getDescriptor() {
        return "I";
    }

    @Override
    public String getInternalName() {
        return "I";
    }

    @Override
    public Type deepGenericCopy() {
        return this;
    }

    @Override
    public Set<Type> getAssignableToTypes(CompilationContext compilationContext) {
        return Sets.newHashSet(
                JVMObjectType.INSTANCE
        );
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public void castFrom(Expression expression, JVMIntType type, CompilationContext compilationContext) {

    }

    @Override
    public void castFrom(Expression expression, JVMFloatType type, CompilationContext compilationContext) {
        expression.getParent().replaceWith(expression, new Float32ToInt32Node(expression));
    }

    @Override
    public void castFrom(Expression expression, JVMObjectType type, CompilationContext compilationContext) {
        expression.getParent().replaceWith(expression, new ObjectToInt32Node(expression));
    }

    @Override
    public void castFrom(Expression expression, IntegerWrapperType type, CompilationContext compilationContext) {
        expression.getParent().replaceWith(expression, new IntegerWrapperToInt32Node(expression));
    }

    @Override
    public void castFrom(Expression expression, Type type, CompilationContext compilationContext) {

    }
}
