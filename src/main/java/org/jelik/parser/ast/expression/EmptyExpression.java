package org.jelik.parser.ast.expression;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.context.NodeContext;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class EmptyExpression extends Expression {

    public static final EmptyExpression INSTANCE = new EmptyExpression();

    private final NodeContext nodeContext = new NodeContext();

    @Override
    public NodeContext getNodeContext() {
        return nodeContext;
    }

    @Override
    public void setType(Type type) {

    }

    @Override
    public void setGenericType(Type type) {

    }

    @Override
    public Type getReturnType() {
        return JVMVoidType.INSTANCE;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

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
