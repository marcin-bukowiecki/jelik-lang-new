package org.jelik.parser.ast.types;

import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.context.TypedNodeContext;
import org.jelik.types.Type;

/**
 * Base class for parsed Type
 *
 * @author Marcin Bukowiecki
 */
public abstract class TypeNode extends ASTNode {

    protected final TypedNodeContext nodeContext = new TypedNodeContext();

    public TypeNode() {
    }

    public Type getType() {
        return nodeContext.getType();
    }

    public void setType(Type type) {
        nodeContext.setType(type);
    }

    public Type getGenericType() {
        return nodeContext.getGenericType();
    }

    public void setGenericType(Type type) {
        nodeContext.setGenericType(type);
    }

    public abstract String getSymbol();
}
