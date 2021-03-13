package org.jelik.parser.ast.context;

import lombok.EqualsAndHashCode;
import org.jelik.types.Type;

/**
 * Context holder for nodes which hold Type i.e. {@link org.jelik.parser.ast.functions.FunctionCallExpr}
 */
@EqualsAndHashCode(callSuper = true)
public class TypedNodeContext extends NodeContext {

    /**
     * i.e. Stream<T>
     */
    private Type type;

    /**
     * i.e. Stream<Int>
     */
    private Type genericType;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getGenericType() {
        return genericType;
    }

    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }
}
