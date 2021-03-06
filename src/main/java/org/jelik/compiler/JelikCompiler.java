package org.jelik.compiler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jelik.compiler.asm.visitor.ToByteCodeResult;
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor;
import org.jelik.compiler.cl.JelikClassLoader;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.data.JavaClassData;
import org.jelik.compiler.helper.JelikSourceFile;
import org.jelik.compiler.helper.SourceFiles;
import org.jelik.compiler.helper.SourceFilesProvider;
import org.jelik.compiler.passes.BasePass;
import org.jelik.compiler.passes.TypeChecker;
import org.jelik.compiler.utils.CompilationWrapper;
import org.jelik.parser.CharPointer;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.Scanner;
import org.jelik.parser.ast.ModuleParser;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.resolvers.*;
import org.jelik.parser.ast.resolvers.types.TypeCastResolver;
import org.jelik.parser.ast.resolvers.types.TypeResolver;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public enum JelikCompiler {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(Jlkc.class);

    public final Map<String /*canonical name*/, ClassData> classDataRegister = Maps.newHashMap();

    public void compile(final String[] args) {
        final CompilationContext compilationContext = new CompilationContext();
        final SourceFiles sourceFiles = SourceFilesProvider.INSTANCE.findSourceFiles(compilationContext);
        final List<JavaClassData> javaClassData = sourceFiles.mapToJavaClassData();
        javaClassData.forEach(cd -> classDataRegister.put(cd.getCanonicalName(), cd));
        var byteCodeResult = compile(sourceFiles.getJelikSourceFiles().stream()
                .map(JelikSourceFile::getFile).collect(Collectors.toList()));

        var outputDirectory = compilationContext.outputDirectory();
        final Path outputDirectoryPath = Path.of(outputDirectory);
        byteCodeResult.forEach(toByteCodeResult -> {
            try {
                Files.write(outputDirectoryPath, toByteCodeResult.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<ToByteCodeResult> compile(List<File> filesToCompile,
                                          CompilationContext compilationContext) {
        List<ModuleDeclaration> parsedModules = Lists.newArrayList();

        for (File file : filesToCompile) {
            CharPointer charPointer = new CharPointer(file);
            Scanner scanner = new Scanner(charPointer);
            Lexer lexer = new Lexer(scanner);
            ParseContext parseContext = new ParseContext(lexer);

            String absolutePath = file.getAbsolutePath();

            var md = new ModuleParser(absolutePath).visit(parseContext);
            parsedModules.add(md);
        }

        return compileModules(parsedModules, compilationContext);
    }


    public List<ToByteCodeResult> compileModules(List<ModuleDeclaration> parsedModules,
                                                 CompilationContext compilationContext) {

        ModuleSignatureResolver moduleSignatureResolver = new ModuleSignatureResolver(this);
        for (var parsedModule : parsedModules) {
            moduleSignatureResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        ImportsResolver importsResolver = new ImportsResolver();
        for (var parsedModule : parsedModules) {
            importsResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        FunctionSignatureResolver functionSignatureResolver = new FunctionSignatureResolver();
        for (var parsedModule : parsedModules) {
            functionSignatureResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        LocalVariableAndLiteralResolver localVariableAndLiteralResolver = new LocalVariableAndLiteralResolver();
        for (var parsedModule : parsedModules) {
            localVariableAndLiteralResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        TypeResolver typeResolver = new TypeResolver();
        for (var parsedModule : parsedModules) {
            typeResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        TypeChecker typeChecker = compilationContext.getPassManager().getPass(TypeChecker.class);
        assert typeChecker != null;
        for (var parsedModule : parsedModules) {
            typeChecker.visitModuleDeclaration(parsedModule, compilationContext);
        }
        if (checkForProblems(compilationContext)) return Collections.emptyList();

        TypeCastResolver typeCastResolver = new TypeCastResolver();
        for (var parsedModule : parsedModules) {
            typeCastResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        BlockLabelsResolver blockLabelsResolver = new BlockLabelsResolver();
        for (var parsedModule : parsedModules) {
            blockLabelsResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        JumpLabelsResolver jumpLabelsResolver = new JumpLabelsResolver();
        for (var parsedModule : parsedModules) {
            jumpLabelsResolver.visitModuleDeclaration(parsedModule, compilationContext);
        }

        final List<BasePass> allPasses = compilationContext.getPassManager().getAllPasses();
        for (var parsedModule : parsedModules) {
            allPasses.forEach(pass -> parsedModule.accept(pass, compilationContext));
        }
        if (checkForProblems(compilationContext)) return Collections.emptyList();

        List<ToByteCodeResult> toByteCodeResults = Lists.newArrayList();
        for (var parsedModule : parsedModules) {
            ToByteCodeVisitor toByteCodeVisitor = new ToByteCodeVisitor();
            toByteCodeVisitor.visitModuleDeclaration(parsedModule, compilationContext);
            toByteCodeResults.addAll(toByteCodeVisitor.getToByteCodeResult());
        }

        return toByteCodeResults;
    }

    public Optional<ClassData> findClassData(String canonicalName, CompilationContext compilationContext) {
        try {
            var classData = classDataRegister.get(canonicalName);
            if (classData == null) {
                var clazz = Class.forName(canonicalName);
                return Optional.ofNullable(createJavaClassData(clazz, compilationContext));
            }
            return Optional.of(classData);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @Nullable
    public ClassData createJavaClassData(Class<?> clazz, CompilationContext compilationContext) {
        if (clazz == null) {
            return null;
        }
        final String canonicalName = clazz.getCanonicalName();
        if (classDataRegister.containsKey(canonicalName)) {
            return classDataRegister.get(canonicalName);
        }
        JavaClassData javaClassData = new JavaClassData(clazz);
        classDataRegister.put(canonicalName, javaClassData);
        javaClassData
                .resolveParentScope(clazz, this, compilationContext);
        javaClassData.resolveMethodScope(clazz);
        javaClassData
                .resolveInterfaceScope(clazz, this, compilationContext);
        return javaClassData;
    }

    @NotNull
    public Type getTypeOrLoad(Class<?> clazz, CompilationContext compilationContext) {
        final String canonicalName = clazz.getCanonicalName();
        final ClassData classData = classDataRegister.get(canonicalName);
        return Objects.requireNonNullElseGet(classData, () -> Objects.requireNonNull(createJavaClassData(clazz, compilationContext))).getType();
    }

    public PrintStream getErrOut() {
        return System.err;
    }

    public boolean isInterface(Type type) {
        if (classDataRegister.containsKey(type.getCanonicalName())) {
            return classDataRegister.get(type.getCanonicalName()).isInterface();
        }
        try {
            return Class.forName(type.getCanonicalName()).isInterface();
        } catch (Exception ignored) {

        }
        return false;
    }

    /**
     * Load Jelik libs
     *
     * @param session given compiler session
     */
    public void loadJelikDependencies(JelikCompileSession session) {

    }

    public void findMainAndRun(List<ToByteCodeResult> compilationResult, JelikCompileSession session) {
        for (ToByteCodeResult toByteCodeResult : compilationResult) {
            if (toByteCodeResult.isMain()) {
                final JelikClassLoader jelikClassLoader = session.getJelikClassLoader();
                final Class<?> aClass = jelikClassLoader.defineClass(
                        toByteCodeResult.getNameForClassLoader(),
                        toByteCodeResult.getBytes()
                );
                Arrays.stream(aClass.getMethods()).filter(m -> m.getName().equals("main")).findFirst().ifPresent(m -> {
                    try {
                        m.invoke(null, (Object) new String[]{});
                    } catch (Exception e) {
                        log.error("Could not invoke main", e);
                    }
                });
                break;
            }
        }
    }

    private boolean checkForProblems(CompilationContext compilationContext) {
        if (compilationContext.getProblemHolder().hasProblems()) {
            compilationContext.getProblemHolder().markProblems();
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public List<ToByteCodeResult> compile(File fileToCompile) {
        return compile(fileToCompile, new CompilationContext());
    }

    @VisibleForTesting
    public List<ToByteCodeResult> compile(File fileToCompile, CompilationContext compilationContext) {
        return CompilationWrapper.wrap(() -> compile(Collections.singletonList(fileToCompile), compilationContext));
    }

    @VisibleForTesting
    public List<ToByteCodeResult> compile(List<File> filesToCompile) {
        return compile(filesToCompile, new CompilationContext());
    }

    //TODO recursive load
    @VisibleForTesting
    public void loadSdk(JelikClassLoader jelikClassLoader) {
        final File file = new File("./sdk/build/jelik/lang/JelikUtils.class");
        if (!file.exists()) {
            throw new IllegalArgumentException("Unresolved sdk file JelikUtils.class in sdk/build");
        }
        try {
            var bytes = Files.readAllBytes(file.toPath());
            final ClassReader classReader = new ClassReader(bytes);
            final String className = classReader.getClassName();
            jelikClassLoader.defineClass(className.replace('/', '.'), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}