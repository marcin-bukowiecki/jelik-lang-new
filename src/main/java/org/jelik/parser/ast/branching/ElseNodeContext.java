package org.jelik.parser.ast.branching;

import org.jelik.parser.ast.context.TypedRefNodeContext;
import org.jelik.parser.ast.labels.LabelNode;

/**
 * @author Marcin Bukowiecki
 */
public class ElseNodeContext extends TypedRefNodeContext {

    private LabelNode finishLabel;

    public void setFinishLabel(LabelNode finishLabel) {
        this.finishLabel = finishLabel;
    }

    public LabelNode getFinishLabel() {
        return finishLabel;
    }
}
