/*
 * Copyright 2019 Marcin Bukowiecki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.MulExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class MulOperator extends AbstractOperator {

    public MulOperator(int offset) {
        super("*", offset, ElementType.mul);
    }

    @Override
    public void accept(@NotNull TokenVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visitMul(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new MulExpr(left, this, right);
    }
}
