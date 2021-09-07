package org.jelik.compiler.exceptions;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.token.Token;

/**
 * @author Marcin Bukowiecki
 */
public class SyntaxException extends CompileException {

    public SyntaxException(String message, Token token, ParseContext parseContext) {
        super(message, token, parseContext.getCurrentFilePath());
    }

    public SyntaxException(String message, Token token, String fileAbsolutePath) {
        super(message, token, fileAbsolutePath);
    }

    public SyntaxException(String message, ASTNode astNode, String fileAbsolutePath) {
        super(message, astNode, fileAbsolutePath);
    }

    public SyntaxException(String message, ASTNode astNode, ClassDeclaration classDeclaration) {
        super(message, astNode, classDeclaration);
    }

    public SyntaxException(String message, ASTNode astNode, CompilationContext compilationContext) {
        super(message, astNode, compilationContext.getCurrentModule());
    }
}
