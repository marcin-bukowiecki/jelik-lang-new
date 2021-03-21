package org.jelik;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.iterators.ReverseListIterator;
import org.jelik.compiler.JelikCompiler;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.labels.LabelNode;
import org.jelik.parser.ast.resolvers.JumpLabelContext;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class CompilationContext {

    private final LinkedList<CompilationUnit> compilationStack = Lists.newLinkedList();

    public final LinkedList<BasicBlock> blockStack = Lists.newLinkedList();

    public final LinkedList<LabelNode> labelStack = Lists.newLinkedList();

    public final LinkedList<JumpLabelContext> jumpLabelContexts = Lists.newLinkedList();

    public CompilationContext() {
    }

    public void pushCompilationUnit(CompilationUnit compilationUnit) {
        compilationStack.addLast(compilationUnit);
    }

    public CompilationUnit popCompilationUnit() {
        return compilationStack.removeLast();
    }

    public CompilationUnit currentCompilationUnit() {
        return compilationStack.getLast();
    }

    public ClassDeclaration getCurrentModule() {
        var compilationUnitListIterator = compilationStack.descendingIterator();
        while (compilationUnitListIterator.hasNext()) {
            CompilationUnit previous = compilationUnitListIterator.next();
            if (previous instanceof ClassDeclaration) {
                return ((ClassDeclaration) previous);
            }
        }
        return null;
    }

    @NotNull
    public LabelNode createLabel(String name) {
        return new LabelNode(((FunctionDeclaration) currentCompilationUnit()).getFunctionContext().getAndIncrementLabel(), name);
    }

    public FunctionDeclaration currentFunction() {
        return ((FunctionDeclaration) compilationStack.getLast());
    }

    public Optional<ClassData> findClassData(String canonicalName) {
        return JelikCompiler.INSTANCE.findClassData(canonicalName, this);
    }

    public Optional<LocalVariable> findLocal(String text) {
        if (!blockStack.isEmpty()) {
            ReverseListIterator<BasicBlock> codeEventReverseListIterator = new ReverseListIterator<BasicBlock>(blockStack);
            while (codeEventReverseListIterator.hasNext()) {
                BasicBlock next = codeEventReverseListIterator.next();
                Optional<LocalVariable> local = next.getBlockContext().findLocal(text);
                if (local.isPresent()) {
                    return local;
                }
            }
        }
        return Optional.empty();
    }

    public String getCompileDirectory() {
        return "";
    }

    public String outputDirectory() {
        return "";
    }
}
