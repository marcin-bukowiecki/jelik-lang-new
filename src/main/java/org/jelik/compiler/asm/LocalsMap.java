package org.jelik.compiler.asm;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

public class LocalsMap {

    private final List<String> localsType = Lists.newArrayList();

    public void addLocal(String descriptor) {
        localsType.add(descriptor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalsMap localsMap = (LocalsMap) o;
        return localsType.equals(localsMap.localsType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localsType);
    }

    @Override
    public String toString() {
        var acc = new StringBuilder("{");

        int i = 0;
        for (String stackType : localsType) {
            acc.append(i).append("->").append(stackType);
            i++;
        }

        return acc.toString();
    }
}
