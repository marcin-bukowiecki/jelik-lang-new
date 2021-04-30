package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.ASTNode;
import org.jelik.types.Type;
import org.jetbrains.annotations.Nullable;

/**
 * @author Marcin Bukowiecki
 */
public interface ExtensionFunctionDeclaration extends ASTNode {

    @Nullable Type getExtOwner();

    @Nullable Type getGenericExtOwner();
}
