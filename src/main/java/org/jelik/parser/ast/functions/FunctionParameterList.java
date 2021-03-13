package org.jelik.parser.ast.functions;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.RightParenthesisToken;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionParameterList extends ASTNode {

    public static final FunctionParameterList EMPTY = new FunctionParameterList(new LeftParenthesisToken(-1, -1),
            Collections.emptyList(), new RightParenthesisToken(-1, -1));

    private final LeftParenthesisToken leftParenthesisToken;

    private final List<FunctionParameter> functionParameters;

    private final RightParenthesisToken rightParenthesisToken;

    public FunctionParameterList(LeftParenthesisToken leftParenthesisToken,
                                 List<FunctionParameter> functionParameters,
                                 RightParenthesisToken rightParenthesisToken) {
        this.leftParenthesisToken = leftParenthesisToken;
        this.functionParameters = functionParameters;
        this.rightParenthesisToken = rightParenthesisToken;
        for (FunctionParameter functionParameter : functionParameters) {
            functionParameter.parent = this;
        }
    }

    public LeftParenthesisToken getLeftParenthesisToken() {
        return leftParenthesisToken;
    }

    public RightParenthesisToken getRightParenthesisToken() {
        return rightParenthesisToken;
    }

    public List<FunctionParameter> getFunctionParameters() {
        return functionParameters;
    }

    @Override
    public String toString() {
        return leftParenthesisToken.toString() + functionParameters.stream().map(FunctionParameter::toString).collect(Collectors.joining("")) + rightParenthesisToken.toString();
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitFunctionParameterList(this, compilationContext);
    }
}
