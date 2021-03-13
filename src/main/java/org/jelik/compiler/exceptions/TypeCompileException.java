package org.jelik.compiler.exceptions;

import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.classes.ClassDeclaration;

/**
 * @author Marcin Bukowiecki
 */
public class TypeCompileException extends CompileException {

    public TypeCompileException(String message, ASTNode node, ClassDeclaration place) {
        super(message, node, place);
    }
}
