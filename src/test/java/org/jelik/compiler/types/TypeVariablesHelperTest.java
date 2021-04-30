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

package org.jelik.compiler.types;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Index;
import org.jelik.types.JelikTypeVariable;
import org.jelik.types.Type;
import org.jelik.types.resolver.TypeVariablesHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Bukowiecki
 */
public class TypeVariablesHelperTest {

    @Test
    public void shouldResolveTypeVariablesForMap() {
        List<Type> actual = TypeVariablesHelper.resolveTypeVariables(Map.class);

        Assertions.assertThat(actual)
                .hasSize(2)
                .contains(new JelikTypeVariable("K"), Index.atIndex(0))
                .contains(new JelikTypeVariable("V"), Index.atIndex(1));
    }

    @Test
    public void shouldResolveTypeVariablesForList() {
        List<Type> actual = TypeVariablesHelper.resolveTypeVariables(List.class);

        Assertions.assertThat(actual)
                .hasSize(1)
                .contains(new JelikTypeVariable("E"), Index.atIndex(0));
    }

    @Test
    public void shouldResolveTypeVariablesForArrayList() {
        List<Type> actual = TypeVariablesHelper.resolveTypeVariables(ArrayList.class);

        Assertions.assertThat(actual)
                .hasSize(1)
                .contains(new JelikTypeVariable("E"), Index.atIndex(0));
    }

    @Test
    public void shouldResolveTypeVariablesForString() {
        List<Type> actual = TypeVariablesHelper.resolveTypeVariables(String.class);

        Assertions.assertThat(actual)
                .hasSize(0);
    }
}
