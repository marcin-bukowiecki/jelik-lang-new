package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.labels.LabelNode;

/**
 * Base type for function body
 *
 * @author Marcin Bukowiecki
 */
public interface FunctionBody extends ASTNode {

    BasicBlockImpl getBasicBlock();

    LabelNode getStartLabel();
}
