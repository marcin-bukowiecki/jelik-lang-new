package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jelik.types.Type;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class ConstructorDeclaration extends FunctionDeclaration {

    public ConstructorDeclaration(Token keyword,
                                  FunctionParameterList functionParameterList,
                                  FunctionReturn functionReturn,
                                  FunctionBody functionBody,
                                  List<TypeNode> generics) {
        super(keyword, new LiteralToken(-1, -1, "<init>"), functionParameterList, functionReturn, functionBody, generics);
    }

    @Override
    public Type getReturnType() {
        return ((ClassDeclaration) parent).getType();
    }

    @Override
    public Type getGenericReturnType() {
        return ((ClassDeclaration) parent).getType();
    }
}
