package org.jelik.parser.ast.context;

import org.jelik.parser.ast.types.AbstractTypeRef;

/**
 * Context holder for nodes which are typed but the don't "hold" a Type,
 * i.e. {@link org.jelik.parser.ast.functions.LambdaDeclarationExpression}.
 * Given nodes just reference a type
 *
 * @author Marcin Bukowiecki
 */
public class TypedRefNodeContext extends NodeContext {

    private AbstractTypeRef typeRef;

    public void setTypeRef(AbstractTypeRef typeRef) {
        this.typeRef = typeRef;
    }

    public AbstractTypeRef getTypeRef() {
        return typeRef;
    }
}
