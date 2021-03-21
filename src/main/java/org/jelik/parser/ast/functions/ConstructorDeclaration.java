package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.types.TypeParameterListNode;
import org.jelik.parser.token.EmptyToken;
import org.jelik.parser.token.Token;
import org.jelik.types.Type;

/**
 * @author Marcin Bukowiecki
 */
public class ConstructorDeclaration extends FunctionDeclaration {

    public ConstructorDeclaration(Token keyword,
                                  FunctionParameterList functionParameterList,
                                  FunctionBody functionBody) {
        super(keyword,
                EmptyToken.INSTANCE,
                functionParameterList,
                MockFunctionReturn.INSTANCE,
                functionBody,
                TypeParameterListNode.Companion.getEMPTY());
    }

    @Override
    public boolean isConstructor() {
        return true;
    }

    @Override
    public Type getReturnType() {
        return ((ClassDeclaration) parent).getType();
    }

    @Override
    public Type getGenericReturnType() {
        return ((ClassDeclaration) parent).getType();
    }

    @Override
    public String toString() {
        return "" + getKeyword() + getFunctionParameterList() + getFunctionBody();
    }
}
