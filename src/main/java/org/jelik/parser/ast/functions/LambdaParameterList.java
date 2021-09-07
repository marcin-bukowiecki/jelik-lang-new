package org.jelik.parser.ast.functions;

import org.jelik.compiler.CompilationContext;
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
public class LambdaParameterList extends FunctionParameterList {

    public static final LambdaParameterList EMPTY = new LambdaParameterList(Collections.emptyList());

    public LambdaParameterList(List<FunctionParameter> functionParameters) {
        super(new LeftParenthesisToken(-1), functionParameters, new RightParenthesisToken(-1));
    }

    @Override
    public String toString() {
        return functionParameters.stream().map(FunctionParameter::toString).collect(Collectors.joining(" "));
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitLambdaParameterList(this, compilationContext);
    }
}
