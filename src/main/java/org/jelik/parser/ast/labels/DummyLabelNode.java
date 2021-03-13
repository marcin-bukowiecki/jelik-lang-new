package org.jelik.parser.ast.labels;

/**
 * @author Marcin Bukowiecki
 */
public class DummyLabelNode extends LabelNode {

    public DummyLabelNode() {
        super(-1, "dummy");
    }

    @Override
    public String toString() {
        return "DUMMY_LABEL";
    }
}
