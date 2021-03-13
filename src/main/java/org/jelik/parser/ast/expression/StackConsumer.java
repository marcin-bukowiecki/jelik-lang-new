package org.jelik.parser.ast.expression;

/**
 * Interface for marking a node which won't leave an element on stack i.e. {@link org.jelik.parser.ast.locals.ValueDeclaration}.
 * So we don't need to call pop or pop2.
 *
 * @author Marcin Bukowiecki
 */
public interface StackConsumer {
}
