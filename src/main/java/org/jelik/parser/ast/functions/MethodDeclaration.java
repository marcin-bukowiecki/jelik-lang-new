package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;

/**
 * @author Marcin Bukowiecki
 */
public class MethodDeclaration extends FunctionDeclaration {

    public MethodDeclaration(Token keyword,
                             LiteralToken name,
                             FunctionParameterList functionParameterList,
                             FunctionReturn functionReturn,
                             FunctionBody functionBody,
                             TypeVariableListNode typeParameterListNode) {
        super(keyword, name, functionParameterList, functionReturn, functionBody, typeParameterListNode);
    }
}
