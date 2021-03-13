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

package org.jelik.compiler;

import java.lang.reflect.Method;

public class FunctionInvoker {

    private final Method method;

    private FunctionInvoker(Method method) {
        this.method = method;
    }

    public static FunctionInvoker method(Class<?> clazz, String name, Class<?>... argTypes) {
        try {
            return new FunctionInvoker(clazz.getDeclaredMethod(name, argTypes));
        } catch (Exception e) {
            throw new RuntimeException("Method not found: " + name, e);
        }
    }

    public Object invoke(Object... args) {
        try {
            return method.invoke(null, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object invokeAndHandle(Class<? extends Throwable> exceptionToHandle, Object... args) {
        try {
            return method.invoke(null, args);
        } catch (Throwable e) {
            if (e.getCause().getClass().equals(exceptionToHandle)) {
                return true;
            }
            e.printStackTrace();
            return false;
        }
    }

    public Object invokeInstance(Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
