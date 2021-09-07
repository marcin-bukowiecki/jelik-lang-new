package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.functions.FunctionBody;
import org.jetbrains.annotations.NotNull;

public class BlockResolver extends AstVisitor {

    @Override
    public void visit(@NotNull FunctionBody fb, @NotNull CompilationContext compilationContext) {

    }
}
