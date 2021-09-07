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

package org.jelik.parser.ast.types;

import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.types.Type;

import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class JelikWildCardType extends Type {

    private List<Type> upperBounds;

    private List<Type> lowerBounds;

    public JelikWildCardType() {
        super("?", "?", TypeEnum.wildCard);
        this.upperBounds = Collections.emptyList();
        this.lowerBounds = Collections.emptyList();
    }

    public JelikWildCardType(WildcardType wildcardType) {
        super("?", "?", TypeEnum.wildCard);
        this.upperBounds = Arrays.stream(wildcardType.getUpperBounds()).map(TypeUtils::createGenericType).collect(Collectors.toList());
        this.lowerBounds = Arrays.stream(wildcardType.getLowerBounds()).map(TypeUtils::createGenericType).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return (upperBounds.isEmpty() ? "" : " :> " + upperBounds.stream().map(Type::toString).collect(Collectors.joining())) +
               (lowerBounds.isEmpty() ? "" : " <: " + lowerBounds.stream().map(Type::toString).collect(Collectors.joining()));
    }

    public boolean containsTypeInBounds(Type type) {
        return upperBounds.contains(type) || lowerBounds.contains(type);
    }

    @Override
    public Type deepGenericCopy() {
        JelikWildCardType jelikWildCardType = new JelikWildCardType();
        jelikWildCardType.upperBounds = upperBounds.stream().map(Type::deepGenericCopy).collect(Collectors.toList());
        jelikWildCardType.lowerBounds = lowerBounds.stream().map(Type::deepGenericCopy).collect(Collectors.toList());
        return jelikWildCardType;
    }

    public List<Type> getLowerBounds() {
        return lowerBounds;
    }

    public List<Type> getUpperBounds() {
        return upperBounds;
    }
}
