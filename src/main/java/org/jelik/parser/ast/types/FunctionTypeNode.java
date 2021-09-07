package org.jelik.parser.ast.types;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.PipeToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionTypeNode extends TypeNode {

    private final PipeToken left;
    private final List<TypeNode> parameterTypes;
    private final PipeToken right;
    private final Token arrow;
    private final TypeNode returnType;

    public FunctionTypeNode(@NotNull PipeToken left,
                            @NotNull List<@NotNull TypeNode> parameterTypes,
                            @NotNull PipeToken right,
                            @NotNull Token arrow,
                            @NotNull TypeNode returnType) {
        this.left = left;
        this.parameterTypes = parameterTypes;
        this.right = right;
        this.arrow = arrow;
        this.returnType = returnType;
    }

    public List<TypeNode> getParameterTypes() {
        return parameterTypes;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitFunctionType(this, compilationContext);
    }

    @Override
    public String getSymbol() {
        return left +
                parameterTypes.stream().map(TypeNode::getSymbol).collect(Collectors.joining(",")) +
                right + arrow + returnType.getSymbol();
    }

    @Override
    public int getStartOffset() {
        return left.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return returnType.getEndOffset();
    }

    @Override
    public String toString() {
        return left +
                parameterTypes.stream().map(Object::toString).collect(Collectors.joining(",")) +
                right + arrow + returnType;
    }
}
