package org.jelik.compiler.config;

import com.google.common.collect.Lists;
import org.jelik.compiler.JelikCompiler;
import org.jelik.compiler.asm.ClassWriterAdapter;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.compiler.passes.PassManager;
import org.jelik.compiler.passes.ProblemHolder;
import org.jelik.parser.ast.blocks.BasicBlock;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.labels.LabelNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class CompilationContext {

    private final LinkedList<CompilationUnit> compilationStack = Lists.newLinkedList();

    private final LinkedList<ClassWriterAdapter> classWriterStack = Lists.newLinkedList();

    public final LinkedList<BasicBlock> blockStack = Lists.newLinkedList();

    public final LinkedList<LabelNode> labelStack = Lists.newLinkedList();

    private final PassManager passManager = new PassManager(this);

    private final ProblemHolder problemHolder = new ProblemHolder(this);

    private final Config config;

    public CompilationContext() {
        config = Config.Companion.defaultConfig();
    }

    public CompilationContext(final Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    @NotNull
    public PassManager getPassManager() {
        return passManager;
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

    @Nullable
    public BasicBlock currentBlock() {
        return blockStack.getLast();
    }

    public String getCompileDirectory() {
        return "";
    }

    public String outputDirectory() {
        return "";
    }

    public void pushClassWriter(ClassWriterAdapter classWriterAdapter) {
        this.classWriterStack.addLast(classWriterAdapter);
    }

    public ClassWriterAdapter popClassWriter() {
        return classWriterStack.removeLast();
    }

    public ClassWriterAdapter currentClassWriter() {
        return classWriterStack.getLast();
    }

    public ProblemHolder getProblemHolder() {
        return problemHolder;
    }
}
