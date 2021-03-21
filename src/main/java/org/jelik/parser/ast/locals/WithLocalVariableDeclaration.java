package org.jelik.parser.ast.locals;

import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.token.LiteralToken;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public interface WithLocalVariableDeclaration extends WithLocalVariable {

    @NotNull
    TypeNode getTypeNode();

    @NotNull
    LiteralToken getLiteralToken();

    void setLocalVariable(@NotNull LocalVariable localVariable);
}
