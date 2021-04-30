package org.jelik.compiler.locals;

import lombok.Setter;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.resolvers.LocalVariableAccessSymbolResult;
import org.jelik.parser.ast.types.AbstractTypeRef;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.types.Type;

import java.util.Optional;

/**
 * Represents a JVM local variable
 *
 * @author Marcin Bukowiecki
 */
public class LocalVariable {

    //name of local variable
    private final String name;

    //is this local variable functions parameter
    private final boolean parameter;

    /**
     * References {@link TypeNode} or {@link Expression}
     *
     * See:
     * {@link org.jelik.parser.ast.types.TypeNodeRef} or {@link org.jelik.parser.ast.types.InferredTypeRef}
     */
    private AbstractTypeRef typeRef;

    //Index of this local variable in locals table
    public int index;

    //Where this local variable is used for first time
    private LabelNode start;

    //Where this local variable is used for last time
    private LabelNode end;

    public LocalVariable(String name, AbstractTypeRef typeRef, boolean parameter) {
        this.name = name;
        this.parameter = parameter;
        this.typeRef = typeRef;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setStart(LabelNode start) {
        this.start = start;
    }

    public void setEnd(LabelNode end) {
        this.end = end;
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
        return Optional.ofNullable(typeRef.getType()).map(Type::isDouble).orElse(false);
    }

    public boolean isLong() {
        return Optional.ofNullable(typeRef.getType()).map(Type::isLong).orElse(false);
    }

    public void setTypeRef(AbstractTypeRef ref) {
        this.typeRef = ref;
    }

    public boolean isFunctionReference() {
        return this.typeRef.isFunctionReference();
    }

    public String getName() {
        return name;
    }

    public AbstractTypeRef getTypeRef() {
        return typeRef;
    }

    public int getIndex() {
        return index;
    }

    public LabelNode getStart() {
        return start;
    }

    public LabelNode getEnd() {
        return end;
    }

    public FindSymbolResult toFindSymbolResult() {
        return new LocalVariableAccessSymbolResult(this);
    }
}
