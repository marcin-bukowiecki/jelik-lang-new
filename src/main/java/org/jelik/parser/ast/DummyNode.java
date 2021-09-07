package org.jelik.parser.ast;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Dummy node for default field init
 *
 * @author Marcin Bukwoiecki
 */
public class DummyNode extends ASTNodeImpl {

    public static final DummyNode INSTANCE = new DummyNode();

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {

    }
}
