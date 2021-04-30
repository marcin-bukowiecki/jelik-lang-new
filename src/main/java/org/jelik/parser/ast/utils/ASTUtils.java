package org.jelik.parser.ast.utils;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.EmptyAstNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Marcin Bukowiecki
 */
public class ASTUtils {

    @SuppressWarnings("unchecked")
    public static <T extends ASTNode> @Nullable T findParent(@NotNull ASTNode astNode, @NotNull Class<T> clazz) {
        if (clazz.isInstance(astNode)) {
            return (T) astNode;
        }
        if (astNode.getParent() instanceof EmptyAstNode) {
            return null;
        }
        return findParent(astNode.getParent(), clazz);
    }

    public static @Nullable FunctionDeclaration getFunctionDeclaration(@NotNull ASTNode astNode) {
        if (astNode instanceof FunctionDeclaration) {
            return ((FunctionDeclaration) astNode);
        }
        if (astNode.getParent() instanceof EmptyAstNode) {
            return null;
        }
        return getFunctionDeclaration(astNode.getParent());
    }

    public static @Nullable ClassDeclaration getClassDeclaration(@NotNull ASTNode astNode) {
        if (astNode instanceof ClassDeclaration) {
            return ((ClassDeclaration) astNode);
        }
        if (astNode.getParent() instanceof EmptyAstNode) {
            return null;
        }
        return getClassDeclaration(astNode.getParent());
    }

    public static @Nullable ModuleDeclaration getModuleDeclaration(@NotNull ASTNode astNode) {
        if (astNode instanceof ModuleDeclaration) {
            return ((ModuleDeclaration) astNode);
        }
        if (astNode.getParent() instanceof EmptyAstNode) {
            return null;
        }
        return getModuleDeclaration(astNode.getParent());
    }

    public static void checkNewLine(@NotNull Expression currentExpr,
                                    @NotNull Token currentToken,
                                    @NotNull ParseContext parseContext) {

        if (currentExpr.getEndRow() == currentToken.getRow()) {
            throw new SyntaxException("Expected new line before: '" + currentToken.getText() + "'",
                    currentToken,
                    parseContext.getCurrentFilePath());
        }
    }
}
