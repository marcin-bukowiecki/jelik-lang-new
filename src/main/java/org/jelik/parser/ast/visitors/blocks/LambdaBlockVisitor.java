package org.jelik.parser.ast.visitors.blocks;

import org.jelik.parser.token.Token;

/**
 * @author Marcin Bukowiecki
 */
public class LambdaBlockVisitor extends BlockVisitor {

    public LambdaBlockVisitor(Token start) {
        super(start);
    }
}
