package org.jelik.parser.ast.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jelik.parser.ast.types.AbstractTypeRef;

/**
 * Context holder for nodes which are typed but the don't "hold" a Type, i.e. {@link org.jelik.parser.ast.locals.GetLocalNode}.
 * Given nodes just reference a type
 *
 * @author Marcin Bukowiecki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TypedRefNodeContext extends NodeContext {

    private AbstractTypeRef typeRef;
}
