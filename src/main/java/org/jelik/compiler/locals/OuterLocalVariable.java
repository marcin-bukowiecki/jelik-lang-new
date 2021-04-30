package org.jelik.compiler.locals;

/**
 * Represents a JVM local variable
 *
 * @author Marcin Bukowiecki
 */
public class OuterLocalVariable extends LocalVariable {

    private final LocalVariable localVariable;

    public OuterLocalVariable(LocalVariable localVariable) {
        super(localVariable.getName(), localVariable.getTypeRef(), localVariable.isParameter());
        this.localVariable = localVariable;
    }

    public LocalVariable getLocalVariable() {
        return localVariable;
    }
}
