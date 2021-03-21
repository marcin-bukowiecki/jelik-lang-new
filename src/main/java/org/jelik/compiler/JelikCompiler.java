package org.jelik.compiler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jelik.CompilationContext;
import org.jelik.compiler.asm.visitor.ToByteCodeResult;
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor;
import org.jelik.compiler.checkers.TypeChecker;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.data.ClassDataImpl;
import org.jelik.compiler.data.JavaClassData;
import org.jelik.compiler.helper.JelikSourceFile;
import org.jelik.compiler.helper.SourceFiles;
import org.jelik.compiler.helper.SourceFilesProvider;
import org.jelik.parser.CharPointer;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.Scanner;
import org.jelik.parser.ast.ModuleParser;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.resolvers.BlockLabelsResolver;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.parser.ast.resolvers.FunctionSignatureResolver;
import org.jelik.parser.ast.resolvers.ImportsResolver;
import org.jelik.parser.ast.resolvers.JumpLabelsResolver;
import org.jelik.parser.ast.resolvers.LocalVariableAndLiteralResolver;
import org.jelik.parser.ast.resolvers.ModuleSignatureResolver;
import org.jelik.parser.ast.resolvers.TypeCastResolver;
import org.jelik.parser.ast.resolvers.TypeResolver;
import org.jelik.types.Type;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public enum JelikCompiler {

    INSTANCE;

    public final Map<String /*canonical name*/, ClassDataImpl> classDataRegister = Maps.newHashMap();

    public void initializeCompiler() {

    }

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

    @VisibleForTesting
    public List<ToByteCodeResult> compile(List<File> filesToCompile) {
        return compile(filesToCompile, new CompilationContext());
    }

    public List<ToByteCodeResult> compile(List<File> filesToCompile, CompilationContext compilationContext) {
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

        ModuleSignatureResolver moduleSignatureResolver = new ModuleSignatureResolver();
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

        for (var parsedModule : parsedModules) {
            TypeChecker.INSTANCE.visitModuleDeclaration(parsedModule, compilationContext);
        }

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
            ClassDataImpl classData = classDataRegister.get(canonicalName);
            if (classData == null) {
                var clazz = Class.forName(canonicalName);
                JavaClassData javaClassData = new JavaClassData(clazz);
                javaClassData.resolveParentScope(clazz);
                javaClassData.resolveMethodScope(clazz);
                javaClassData.resolveInterfaceScope(clazz);

                classDataRegister.put(canonicalName, javaClassData);
                return Optional.of(javaClassData);
            }
            return Optional.of(classData);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public Optional<Class<?>> findClass(String canonicalName) {
        try {
            Optional<Type> typeOpt = DefaultImportedTypeResolver.getTypeOpt(canonicalName);
            if (typeOpt.isPresent()) {

            }

            var clazz = Class.forName(canonicalName);

            //TODO search
            JavaClassData javaClassData = new JavaClassData(clazz);
            javaClassData.resolveMethodScope(clazz);
            javaClassData.resolveInterfaceScope(clazz);

            classDataRegister.put(canonicalName, javaClassData);

            return Optional.of(clazz);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public PrintStream getErrOut() {
        return System.err;
    }
}
