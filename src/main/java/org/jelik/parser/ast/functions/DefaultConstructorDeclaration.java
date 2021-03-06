package org.jelik.parser.ast.functions;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.keyword.ConstructorKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class DefaultConstructorDeclaration extends ConstructorDeclaration {

    public DefaultConstructorDeclaration() {
        super(new ConstructorKeyword(-1),
                FunctionParameterList.createEmpty(),
                FunctionBodyBlock.createEmpty());
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitDefaultConstructor(this, compilationContext);
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public @NotNull String getDescriptor() {
        return "()V";
    }

    @Override
    public String toString() {
        return "";
    }
}
