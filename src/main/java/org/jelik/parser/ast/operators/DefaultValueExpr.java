package org.jelik.parser.ast.operators;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.utils.ASTDataKey;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.operators.DefaultValueOperator;
import org.jetbrains.annotations.NotNull;

/**
 * Represents ?: operator expr
 *
 * @author Marcin Bukowiecki
 */
public class DefaultValueExpr extends AbstractOpExpr {

    public DefaultValueExpr(Expression left, DefaultValueOperator op, Expression right) {
        super(left, op, right);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitDefaultValueExpr(this, compilationContext);
    }

    public void setNullLabel(LabelNode label) {
        dataHolder.putData(ASTDataKey.NULL_LABEL, label);
    }

    public void setNotNullLabel(LabelNode label) {
        dataHolder.putData(ASTDataKey.NOT_NULL_LABEL, label);
    }

    public LabelNode getNullLabel() {
        return dataHolder.getData(ASTDataKey.NULL_LABEL);
    }

    public LabelNode getNotNullLabel() {
        return dataHolder.getData(ASTDataKey.NOT_NULL_LABEL);
    }
}
