package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.types.TypeNode;
import org.jelik.types.Type;

/**
 * Base class for function body
 *
 * @author Marcin Bukowiecki
 */
public abstract class FunctionBody extends TypeNode {

    @Override
    public void setType(Type type) {
        this.nodeContext.setType(type);
    }

    @Override
    public Type getType() {
        return nodeContext.getType();
    }
}
