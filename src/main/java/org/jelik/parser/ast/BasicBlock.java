package org.jelik.parser.ast;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.blocks.BlockContext;
import org.jelik.parser.ast.expression.ExpressionReferencingType;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class BasicBlock extends ExpressionReferencingType {

    public static final BasicBlock EMPTY = new BasicBlock(Collections.emptyList());

    private final List<Expression> expressions;

    @Getter
    private final BlockContext blockContext = new BlockContext();

    public BasicBlock(List<Expression> expressions) {
        this.expressions = expressions;
        for (Expression expression : expressions) {
            expression.setParent(this);
        }
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public String toString() {
        return expressions.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
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
        for (Expression expression : expressions) {
            if (expression == oldNode) {
                break;
            }
            index++;
        }
        expressions.set(index, newNode);
    }
}
