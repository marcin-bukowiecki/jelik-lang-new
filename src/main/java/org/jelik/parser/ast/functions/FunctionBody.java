package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for function body
 *
 * @author Marcin Bukowiecki
 */
public abstract class FunctionBody extends TypeNode {

    public FunctionBody() { }

    public abstract BasicBlockImpl getBasicBlock();

    @Override
    public void setType(@NotNull Type type) {
        this.nodeContext.setType(type);
    }

    @Override
    public Type getType() {
        return nodeContext.getType();
    }

    public abstract LabelNode getStartLabel();
}
