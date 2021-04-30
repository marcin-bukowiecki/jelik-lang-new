package org.jelik.parser.ast.functions;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.arguments.Argument;
import org.jelik.parser.ast.arguments.ArgumentList;
import org.jelik.parser.ast.expression.ExpressionWithType;
import org.jelik.parser.ast.functions.providers.TargetFunctionCallProvider;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.SuperKeyword;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class SuperCallExpr extends ExpressionWithType implements FunctionCall {

    private final SuperKeyword superKeyword;

    private final ArgumentList argumentList;

    protected TargetFunctionCallProvider<?> targetFunctionCall;

    private final Type owner;

    public SuperCallExpr(SuperKeyword superKeyword, ArgumentList argumentList, Type owner) {
        this.superKeyword = superKeyword;
        this.argumentList = argumentList;
        this.owner = owner;
        argumentList.setParent(this);
        nodeContext.setType(JVMVoidType.INSTANCE);
        nodeContext.setGenericType(JVMVoidType.INSTANCE);
    }

    public SuperKeyword getSuperKeyword() {
        return superKeyword;
    }

    public ArgumentList getArgumentList() {
        return argumentList;
    }

    public TargetFunctionCallProvider<?> getTargetFunctionCall() {
        return targetFunctionCall;
    }

    @Override
    public int getStartRow() {
        return superKeyword.getRow();
    }

    @Override
    public int getStartCol() {
        return superKeyword.getCol();
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
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visit(this, compilationContext);
    }

    @Override
    public String toString() {
        return superKeyword.toString() + argumentList.toString();
    }

    public void setTargetFunctionCall(TargetFunctionCallProvider<?> targetFunctionCall) {
        this.targetFunctionCall = targetFunctionCall;
    }

    @Override
    public void replaceWith(@NotNull Expression oldNode, @NotNull Expression newNode) {

    }

    @Override
    public Type getOwner() {
        return owner;
    }

    @Override
    public List<Type> getArgumentTypes() {
        return getArgumentList().getArguments().stream().map(Argument::getReturnType).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "<init>";
    }
}
