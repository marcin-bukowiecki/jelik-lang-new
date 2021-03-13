package org.jelik.compiler.asm;

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.Objects;

public class StackMap {

    private final LinkedList<String> stackTypes = Lists.newLinkedList();

    public StackMap() {
    }

    public void pushOnStack(String descriptor) {
        stackTypes.addLast(descriptor);
    }

    public String popFromStack() {
        return stackTypes.removeLast();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StackMap stackMap = (StackMap) o;
        return stackTypes.equals(stackMap.stackTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stackTypes);
    }

    @Override
    public String toString() {
        if (stackTypes.isEmpty()) {
            return "";
        }

        var acc = new StringBuilder("{");

        int i = 0;
        for (String stackType : stackTypes) {
            acc.append(i).append("->").append(stackType);
            i++;
        }

        return acc.toString();
    }

    public String last() {
        return stackTypes.getLast();
    }
}
