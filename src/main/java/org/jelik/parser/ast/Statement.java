package org.jelik.parser.ast;

import org.jelik.types.Type;

/**
 * Represents:
 *
 * foo.bar.foo()
 * val foo = bar
 * var foo = bar
 * if bar {
 *
 * }
 * when foo {
 *
 * }
 * return bar
 *
 * @author Marcin Bukowiecki
 */
public interface Statement extends ASTNode {

    Type getType();

    Type getGenericType();

    Type getReturnType();

    Type getGenericReturnType();
}
