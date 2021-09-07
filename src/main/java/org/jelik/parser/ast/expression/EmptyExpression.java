package org.jelik.parser.ast.expression;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class EmptyExpression extends ASTNodeImpl implements Expression {

    public static final EmptyExpression INSTANCE = new EmptyExpression();

    @Override
    public Type getReturnType() {
        return JVMVoidType.INSTANCE;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }

    @Override
    public Type getGenericReturnType() {
        return JVMVoidType.INSTANCE;
    }

    @Override
    public Type getType() {
        return JVMVoidType.INSTANCE;
    }

    @Override
    public Type getGenericType() {
        return JVMVoidType.INSTANCE;
    }

    @Override
    public String toString() {
        return "";
    }
}
