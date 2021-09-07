package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.types.VoidTypeNode;
import org.jelik.parser.token.ArrowToken;

/**
 * @author Marcin Bukowiecki
 */
public class VoidFunctionReturn extends FunctionReturn {

    public static final VoidFunctionReturn INSTANCE = new VoidFunctionReturn();

    public VoidFunctionReturn() {
        super(new ArrowToken(-1), new VoidTypeNode());
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public String toString() {
        return "";
    }
}
