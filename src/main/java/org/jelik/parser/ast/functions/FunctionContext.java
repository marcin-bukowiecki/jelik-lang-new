package org.jelik.parser.ast.functions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.collections4.iterators.ReverseListIterator;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.types.Type;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
@Data
public class FunctionContext {

    private String signature;

    private Map<String, LocalVariable> localVariableNamesMap = Maps.newHashMap();

    private List<TryExpression> tryExpressionList = Lists.newArrayList();

    private List<LocalVariable> localVariableList = Lists.newArrayList();

    private Map<String, TypeNode> typeParametersMap = Maps.newHashMap();

    private Map<String, TypeNode> genericTypeParametersMap = Maps.newHashMap();

    private int localCounter;

    private int labelCounter = 0;

    private LinkedList<CodeEvent> codeEvents = Lists.newLinkedList();

    public void addLocalVariable(LocalVariable localVariable) {
        localVariable.index = localCounter++;
        localVariableNamesMap.put(localVariable.getName(), localVariable);
        localVariableList.add(localVariable);
    }

    public void addCodeEvent(CodeEvent codeEvent) {
        this.codeEvents.addLast(codeEvent);
    }

    public boolean isStringBuilderInitialized() {
        ReverseListIterator<CodeEvent> codeEventReverseListIterator = new ReverseListIterator<>(codeEvents);
        while (codeEventReverseListIterator.hasNext()) {
            CodeEvent next = codeEventReverseListIterator.next();
            if (next == CodeEvent.stringBuilderInitialized) {
                return true;
            }
        }
        return false;
    }

    public CodeEvent popCodeEvent() {
        return codeEvents.removeLast();
    }

    public List<LocalVariable> getLocalsAsParameters() {
        return this.localVariableList.stream().filter(LocalVariable::isParameter).collect(Collectors.toList());
    }

    public void shiftIndexesAccordingToType() {
        int acc = 0;
        for (LocalVariable localVariable : getLocalVariableList()) {
           localVariable.index = localVariable.index + acc;
           if (localVariable.isLong() || localVariable.isDouble()) {
               acc++;
           }
        }
    }

    public int getAndIncrementLabel() {
        return this.labelCounter++;
    }

    public void addTryCatchBlock(TryExpression tryExpression) {
        tryExpressionList.add(tryExpression);
    }

    public List<TryExpression> getTryExpressionList() {
        return Collections.unmodifiableList(tryExpressionList);
    }

    public Map<String, TypeNode> getGenericTypesMap() {
        return typeParametersMap;
    }

    public Map<String, TypeNode> getGenericTypeParametersMappings() {
        return genericTypeParametersMap;
    }
}
