package org.jelik.parser.ast.functions;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.types.InferredTypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class InferredLambdaParameter extends FunctionParameter implements LambdaParameter {

    public InferredLambdaParameter(LiteralToken name, Token comma) {
        super(new InferredTypeNode(), name, comma);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitInferredLambdaParameter(this, compilationContext);
    }
}
