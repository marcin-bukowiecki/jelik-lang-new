package org.jelik.compiler.locals;

import lombok.Getter;
import lombok.Setter;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeNodeRef;
import org.jelik.parser.ast.types.TypeRef;
import org.jelik.types.Type;

/**
 * Represents a JVM local variable
 *
 * @author Marcin Bukowiecki
 */
@Getter
public class LocalVariable {

    //name of local variable
    private final String name;

    //is this local variable functions parameter
    private final boolean parameter;

    /**
     * References {@link TypeNode} or {@link org.jelik.parser.ast.Expression}
     *
     * See:
     * {@link org.jelik.parser.ast.types.TypeNodeRef} or {@link org.jelik.parser.ast.types.InferredTypeRef}
     */
    private TypeRef typeRef;

    //Index of this local variable in locals table
    @Setter
    public int index;

    //Where this local variable is used for first time
    @Setter
    private LabelNode start;

    //Where this local variable is used for last time
    @Setter
    private LabelNode end;

    public LocalVariable(String name, TypeRef typeRef, boolean parameter) {
        this.name = name;
        this.parameter = parameter;
        this.typeRef = typeRef;
    }

    public boolean isParameter() {
        return parameter;
    }

    public Type getType() {
        return typeRef.getType();
    }

    public Type getGenericType() {
        return typeRef.getGenericType();
    }

    public String getDescriptor() {
        return getType().getDescriptor();
    }

    //TODO
    public String getSignature() {
        return "";
    }

    public boolean isDouble() {
        return typeRef.getType().isDouble();
    }

    public boolean isLong() {
        return typeRef.getType().isLong();
    }

    public void setTypeRef(TypeNodeRef typeNodeRef) {
        this.typeRef = typeNodeRef;
    }
}
