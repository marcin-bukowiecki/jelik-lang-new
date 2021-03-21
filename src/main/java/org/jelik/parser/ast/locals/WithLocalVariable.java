package org.jelik.parser.ast.locals;

import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.Expression;

import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public interface WithLocalVariable {

    LocalVariable getLocalVariable();

    Expression getFurtherExpression();

    Optional<Expression> getFurtherExpressionOpt();
}
