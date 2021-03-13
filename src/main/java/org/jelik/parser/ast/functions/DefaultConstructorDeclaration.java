package org.jelik.parser.ast.functions;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.ConstructorKeyword;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * @author Marcin Bukowiecki
 */
public class DefaultConstructorDeclaration extends ConstructorDeclaration {

    public DefaultConstructorDeclaration() {
        super(new ConstructorKeyword(-1,-1), FunctionParameterList.EMPTY,
                new MockFunctionReturn(),
                FunctionBodyBlock.EMPTY,
                Collections.emptyList());
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitDefaultConstructor(this, compilationContext);
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public String getDescriptor() {
        return "()V";
    }

    @Override
    public String toString() {
        return "";
    }
}
