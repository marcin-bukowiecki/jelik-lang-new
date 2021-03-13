package org.jelik.parser.ast.functions;

import org.jelik.types.Type;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public interface FunctionCall {

    Type getOwner();

    List<Type> getArgumentTypes();

    String getName();
}
