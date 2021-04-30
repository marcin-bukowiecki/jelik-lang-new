package org.jelik.parser.ast.blocks;

import com.google.common.collect.ImmutableList;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.ReturnExpr;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class BasicBlockImpl extends ASTNodeImpl implements BasicBlock {

    private List<Expression> expressions;

    private final BlockContext blockContext = new BlockContext();

    public BasicBlockImpl(@NotNull List<@NotNull Expression> expressions) {
        this.expressions = expressions;
        for (var expr : expressions) {
            expr.setParent(this);
        }
    }

    public static @NotNull BasicBlockImpl createEmpty() {
        return new BasicBlockImpl(new ArrayList<>());
    }

    @Override
    public @Nullable ASTNode getLast() {
        return getExpressions().isEmpty() ? null : getExpressions().get(getExpressions().size() - 1);
    }

    public @NotNull BlockContext getBlockContext() {
        return blockContext;
    }

    public void prependExpression(@NotNull Expression expression) {
        expression.setParent(this);
        this.expressions = ImmutableList.<Expression>builder().add(expression).addAll(expressions).build();
    }

    public void appendExpression(@NotNull Expression expression) {
        expression.setParent(this);
        this.expressions = ImmutableList.<Expression>builder().addAll(expressions).add(expression).build();
    }

    public @NotNull List<@NotNull Expression> getExpressions() {
        return expressions;
    }

    @Override
    public @NotNull List<? extends ASTNode> getChildren() {
        return expressions;
    }

    @Override
    public String toString() {
        return expressions.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitBasicBlock(this, compilationContext);
    }

    @Override
    public Type getType() {
        if (expressions.isEmpty()) {
            return JVMVoidType.INSTANCE;
        } else {
            return expressions.get(expressions.size() - 1).getType();
        }
    }

    @Override
    public Type getGenericType() {
        if (expressions.isEmpty()) {
            return JVMVoidType.INSTANCE;
        } else {
            return expressions.get(expressions.size() - 1).getGenericType();
        }
    }

    @Override
    public void setType(@NotNull Type type) {

    }

    @Override
    public void setGenericType(@NotNull Type type) {

    }

    @Override
    public Type getReturnType() {
        if (expressions.isEmpty()) {
            return JVMVoidType.INSTANCE;
        } else {
            return expressions.get(expressions.size() - 1).getReturnType();
        }
    }

    @Override
    public Type getGenericReturnType() {
        if (expressions.isEmpty()) {
            return JVMVoidType.INSTANCE;
        } else {
            return expressions.get(expressions.size() - 1).getGenericReturnType();
        }
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {
        int index = 0;
        for (var expr : expressions) {
            if (expr == oldNode) {
                break;
            }
            index++;
        }
        expressions.set(index, newNode);
    }

    public @NotNull FunctionDeclaration getOwningFunction() {
        var parent = this.getParent();
        while (!(parent instanceof FunctionDeclaration)) {
            parent = parent.getParent();
        }
        return (FunctionDeclaration) parent;
    }

    public boolean endsWithReturnStmt() {
        for (var expr : expressions) {
            if (expr instanceof ReturnExpr) {
                return true;
            }
        }
        return false;
    }
}
