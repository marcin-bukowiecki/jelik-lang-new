package org.jelik.parser.ast.functions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.iterators.ReverseListIterator;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.mir.MIRFunction;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.types.TypeNode;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionContext {

    private Map<String, LocalVariable> localVariableNamesMap = Maps.newHashMap();

    private List<TryExpression> tryExpressionList = Lists.newArrayList();

    private List<LocalVariable> localVariableList = Lists.newArrayList();

    private Map<String, TypeNode> typeParametersMap = Maps.newHashMap();

    private Map<String, TypeNode> genericTypeParametersMap = Maps.newHashMap();

    private int localCounter;

    private int labelCounter = 0;

    private LinkedList<CodeEvent> codeEvents = Lists.newLinkedList();

    private MIRFunction mirFunction;

    private final FunctionDeclaration functionDeclaration;

    public FunctionContext(FunctionDeclaration functionDeclaration) {
        this.functionDeclaration = functionDeclaration;
    }

    public Map<String, LocalVariable> getLocalVariableNamesMap() {
        return localVariableNamesMap;
    }

    public List<LocalVariable> getLocalVariableList() {
        return localVariableList;
    }

    public Map<String, TypeNode> getTypeParametersMap() {
        return typeParametersMap;
    }

    public Map<String, TypeNode> getGenericTypeParametersMap() {
        return genericTypeParametersMap;
    }

    public int getLocalCounter() {
        return localCounter;
    }

    public int getLabelCounter() {
        return labelCounter;
    }

    public LinkedList<CodeEvent> getCodeEvents() {
        return codeEvents;
    }

    public MIRFunction getMirFunction() {
        return mirFunction;
    }

    public FunctionDeclaration getFunctionDeclaration() {
        return functionDeclaration;
    }

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

    public Map<String, TypeNode> getTypeParametersMappings() {
        return ImmutableMap.<String, TypeNode>builder()
                .putAll(Collections.unmodifiableMap(typeParametersMap))
                .putAll(functionDeclaration.getParent() == null ? Collections.emptyMap() :
                        Collections.unmodifiableMap(((ClassDeclaration) functionDeclaration.getParent())
                                .getTypeParametersMappings()))
                .build();
    }

    public Map<String, TypeNode> getGenericTypeParametersMappings() {
        return ImmutableMap.<String, TypeNode>builder()
                .putAll(Collections.unmodifiableMap(genericTypeParametersMap))
                .putAll(Collections.unmodifiableMap(((ClassDeclaration) functionDeclaration.getParent()).getGenericTypeParametersMappings()))
                .build();
    }

    public void addTypeParameterMapping(String symbol, TypeNode typeNode) {
        this.typeParametersMap.put(symbol, typeNode);
    }

    public void addGenericTypeParameterMapping(String symbol, TypeNode typeNode) {
        this.genericTypeParametersMap.put(symbol, typeNode);
    }
}
