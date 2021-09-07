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

package org.jelik.compiler.exceptions;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public final class ExceptionQueue {

    private static final ListMultimap<Entry, CompileException> compileExceptions = ArrayListMultimap.create();

    public static boolean hasExceptions() {
        return !compileExceptions.isEmpty();
    }

    public static void printExceptions() {
        compileExceptions.asMap().forEach((e, v) -> v.forEach(CompileException::printErrorMessage));
        clearQueue();
    }

    @VisibleForTesting
    public static void clearQueue() {
        compileExceptions.clear();
    }

    public static void enqueue(CompileException ex) {
        compileExceptions.put(new Entry(ex.getAbsoluteFilePath()), ex);
    }

    public static void error(CompileException ex) {
        enqueue(ex);
    }
/*
    public static void error(String message, ASTNode place) {
        var ex = new CompileException(message, place);
        enqueue(ex);
        if (Boolean.getBoolean("compilerThrowException")) {
            throw ex;
        }
    }

    public static void error(String message, Token place) {
        var ex = new CompileException(message, place);
        enqueue(ex);
        if (Boolean.getBoolean("compilerThrowException")) {
            throw ex;
        }
    }

    public static void panic(String message, Token place) {
        var e = new CompileError(message, place);
        enqueue(e);
        throw e;
    }

    public static void panic(String message, ASTNode node) {
        var e = new CompileError(message, node);
        enqueue(e);
        throw e;
    }*/

    public static class Entry {

        private final String fileName;

        Entry(String fileName) {
            Objects.requireNonNull(fileName);
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(fileName, entry.fileName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fileName);
        }
    }

}
