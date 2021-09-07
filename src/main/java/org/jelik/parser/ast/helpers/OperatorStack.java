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

package org.jelik.parser.ast.helpers;

import com.google.common.collect.ImmutableMap;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.Token;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Marcin Bukowiecki
 */
public final class OperatorStack {

    private static final Map<ElementType, Integer> operatorWeights;

    static {
        operatorWeights = new EnumMap<>(ImmutableMap.<ElementType, Integer>builder()

                .put(ElementType.referenceOperator, 160)

                .put(ElementType.safeNavigation, 160)

                .put(ElementType.combineOperator, 140)
                .put(ElementType.incrOperator, 140)
                //.put(":", 140)
                .put(ElementType.prependOperator, 140)
                .put(ElementType.appendOperator, 140)

                .put(ElementType.add, 110)
                .put(ElementType.sub, 110)

                .put(ElementType.orOperator, 30)
                .put(ElementType.andOperator, 40)

                .put(ElementType.bitwiseAnd, 70)
                .put(ElementType.bitwiseXor, 60)
                .put(ElementType.bitwiseOr, 50)

                .put(ElementType.equalOperator, 80)
                .put(ElementType.notEqualOperator, 80)

                .put(ElementType.notOperator, 140)
                .put(ElementType.decrOperator, 140)

                .put(ElementType.shiftLeft, 100)
                .put(ElementType.signedShiftRight, 100)
                .put(ElementType.unsignedShiftRight, 100)

                .put(ElementType.greaterOperator, 90)
                .put(ElementType.greaterOrEqualOperator, 90)
                .put(ElementType.lesserOperator, 90)
                .put(ElementType.lesserOrEqualOperator, 90)

                .put(ElementType.isOperator, 160)
                .put(ElementType.isNotOperator, 160)

                .put(ElementType.asOperator, 130)
                .put(ElementType.pow, 130)

                .put(ElementType.mul, 120)
                .put(ElementType.divide, 120)
                .put(ElementType.modulo, 120)

                .put(ElementType.arrayCreate, 160)
                .put(ElementType.maybeExpr, 160)
                .put(ElementType.mapCreate, 160)

                .put(ElementType.inOperator, 5)
                .put(ElementType.range, 5)
                .put(ElementType.nullSafeCall, 5)

                .put(ElementType.defaultValue, 4)


                .put(ElementType.assign, 1)


                //.put(ElementType.eqOperator, 0)
                .build());
    }

    public static boolean isHigherPrecedence(Token given, Token than) {
        return operatorWeights.get(given.getTokenType()) > operatorWeights.get(than.getTokenType());
    }
}
