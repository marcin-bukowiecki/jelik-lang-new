package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.collections.ArrayInElementProvider;
import org.jelik.compiler.asm.collections.CollectionInElementProvider;
import org.jelik.compiler.asm.collections.InElementProvider;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.InKeyword;
import org.jelik.types.JVMBooleanType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents e in expr
 *
 * @author Marcin Bukowiecki
 */
public class InExpr extends AbstractOpExpr {

    public InExpr(Expression left, InKeyword inKeyword, Expression right) {
        super(left, inKeyword, right);
        setType(JVMBooleanType.INSTANCE);
        setGenericType(JVMBooleanType.INSTANCE);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitInExpr(this, compilationContext);
    }

    public InKeyword getInKeyword() {
        return ((InKeyword) this.op);
    }

    public InElementProvider containsElementProvider() {
        if (right.getGenericReturnType().isArray()) {
            return new ArrayInElementProvider(this);
        }
        return new CollectionInElementProvider(this);
    }
}
