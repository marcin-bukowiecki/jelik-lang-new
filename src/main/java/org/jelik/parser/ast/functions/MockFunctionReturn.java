package org.jelik.parser.ast.functions;

import org.jelik.parser.ast.types.UndefinedTypeNode;
import org.jelik.parser.token.ArrowToken;

/**
 * @author Marcin Bukowiecki
 */
public class MockFunctionReturn extends FunctionReturn {

    public static final MockFunctionReturn INSTANCE = new MockFunctionReturn();

    public MockFunctionReturn() {
        super(new ArrowToken(-1, -1), UndefinedTypeNode.UNDEFINED_TYPE_NODE);
    }
}
