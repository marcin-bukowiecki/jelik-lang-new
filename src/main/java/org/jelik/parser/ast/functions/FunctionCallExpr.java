package org.jelik.parser.ast.functions;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.DotCallExpr;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.LiteralExpr;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arguments.ArgumentList;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.utils.ASTUtils;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionCallExpr extends ExpressionWithType implements FunctionCall {

    private final LiteralExpr literalExpr;

    private final ArgumentList argumentList;

    protected TargetFunctionCallProvider<?> targetFunctionCall;

    protected Type owner;

    public FunctionCallExpr(LiteralExpr literalExpr, ArgumentList argumentList) {
        this.literalExpr = literalExpr;
        this.argumentList = argumentList;
        literalExpr.setParent(this);
        argumentList.setParent(this);
    }

    public LiteralExpr getLiteralExpr() {
        return literalExpr;
    }

    public ArgumentList getArgumentList() {
        return argumentList;
    }

    public TargetFunctionCallProvider<?> getTargetFunctionCall() {
        return targetFunctionCall;
    }

    public void setOwner(Type owner) {
        this.owner = owner;
    }

    public Type getOwner() {
        return owner;
    }

    @Override
    public int getStartCol() {
        return literalExpr.getStartCol();
    }

    @Override
    public int getEndCol() {
        return argumentList.getEndCol();
    }

    @Override
    public int getEndRow() {
        return argumentList.getEndRow();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return literalExpr.toString() + argumentList.toString() + (furtherExpression == null ? "" : furtherExpression.toString());
    }

    public void setTargetFunctionCall(TargetFunctionCallProvider<?> targetFunctionCall) {
        this.targetFunctionCall = targetFunctionCall;
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {

    }

    public String getName() {
        return literalExpr.getLiteralToken().getText();
    }

    public ASTNode getCaller() {
        if (parent instanceof DotCallExpr) {
            return ((DotCallExpr) parent).getSubject();
        } else {
            return parent;
        }
    }

    @Override
    public List<Type> getArgumentTypes() {
        return getArgumentList().getArguments().stream().map(Argument::getReturnType).collect(Collectors.toList());
    }

    @Override
    public int getStartRow() {
        return literalExpr.getStartRow();
    }
}
