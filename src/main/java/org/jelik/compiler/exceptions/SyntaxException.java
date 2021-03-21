package org.jelik.compiler.exceptions;

import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.token.Token;

/**
 * @author Marcin Bukowiecki
 */
public class SyntaxException extends CompileException {

    public SyntaxException(String message, Token token, String fileAbsolutePath) {
        super(message, token, fileAbsolutePath);
    }

    public SyntaxException(String message, ASTNode astNode, ClassDeclaration classDeclaration) {
        super(message, astNode, classDeclaration);
    }
}
