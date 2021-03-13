package org.jelik.parser.ast.branching;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jelik.parser.ast.context.TypedRefNodeContext;
import org.jelik.parser.ast.labels.LabelNode;

/**
 * @author Marcin Bukowiecki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IfNodeContext extends TypedRefNodeContext {

    private boolean jumpOver;
}
