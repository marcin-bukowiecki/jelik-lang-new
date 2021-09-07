/*
 * Copyright 2019 Marcin Bukowiecki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jelik.compiler;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jelik.compiler.asm.visitor.ToByteCodeResult;
import org.jelik.compiler.cl.JelikClassLoader;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.compiler.exceptions.ExceptionQueue;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.ModuleParser;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entry points for jelik compiler
 *
 * @author Marcin Bukowiecki
 */
public class Jlkc {

    private static final Logger log = LoggerFactory.getLogger(Jlkc.class);

    private static final Options options = new Options();

    static {
        options.addOption("showBC", true, "Shows bytecode");
        options.addOption("compilerEnableLogging", true, "Enable compiler logging");
        options.addOption("compilerPrintStacktrace", true, "Prints the compiler exception stack trace");
        options.addOption("r", "run", false, "Runs the program after compilation (requires a class with main method)");
        options.addOption("runMain", true, "Runs the program starting from given class with main method");
        options.addOption("out", "outputDirectory", true, "Directory to save compiled classes");
        options.addOption("in", "inputDirectory",true, "Input directory with Jelik files");
        options.addOption("lc", "loadClasses",false, "Load compiled classes");
    }

    public static void main(String[] args) {
        //parse terminal compiler parameters
        var parser = new DefaultParser();
        var formatter = new HelpFormatter();

        CommandLine compilerArguments;

        try {
            compilerArguments = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("jlkc", options);
            System.exit(1);
            return; //make sure :)
        }

        validateArgs(compilerArguments);

        var session = createSession(compilerArguments);

        JelikCompiler.INSTANCE.loadJelikDependencies(session);

        var jlkc = new Jlkc();
        var compilationResult = jlkc.compile(session);
        writeOutput(compilationResult, session);

        if (session.shouldRun()) {
            JelikCompiler.INSTANCE.findMainAndRun(compilationResult, session);
        }
    }

    private static void writeOutput(@NotNull List<ToByteCodeResult> compilationResult,
                                    @NotNull  JelikCompileSession session) {
        final String compileOutput = session.getCompileOutput();
        for (ToByteCodeResult toByteCodeResult : compilationResult) {
            try {
                final String packageAsPath = compileOutput + File.separator + toByteCodeResult.getPackageAsPath();
                final Path p = Path.of(packageAsPath);
                Files.createDirectories(p);

                Path path = Paths.get(packageAsPath + File.separator + toByteCodeResult.getSimpleName() + ".class");
                Files.write(
                        path,
                        toByteCodeResult.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //copy sdk to build folder
        final File libPath = session.getLibPath();
        final File sdkFile = new File(libPath.getAbsolutePath() + File.separator + "sdk");
        try {
            FileUtils.copyDirectory(sdkFile, Path.of(compileOutput).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull JelikCompileSession createSession(@NotNull  final CommandLine compilerArguments) {
        var logEnabled = Boolean.parseBoolean(compilerArguments.getOptionValue("compilerEnableLogging"));
        var run = compilerArguments.hasOption("r");
        var lc = compilerArguments.hasOption("lc");
        var classWithMain = compilerArguments.getOptionValue("runMain");
        var printCompilerException = Boolean.parseBoolean(
                compilerArguments.getOptionValue("compilerPrintStacktrace", "false")
        );
        var outputDirectory = compilerArguments.getOptionValue("out");
        var printByteCode = Boolean.parseBoolean(compilerArguments.getOptionValue("showBC", "true"));
        var filesToCompile = compilerArguments.getArgList();

        var inputDirectory = compilerArguments.getOptionValue("in");
        if (StringUtils.isEmpty(inputDirectory)) {
            final var rootDir = System.getProperty("user.dir");
            assert StringUtils.isNotEmpty(rootDir) : "Error: empty user.dir property";
            inputDirectory = rootDir;
        }

        if (StringUtils.isEmpty(outputDirectory)) {
            outputDirectory = inputDirectory + "/build";
        }

        var config = new JelikCompileConfig(
                filesToCompile,
                printCompilerException,
                outputDirectory,
                inputDirectory,
                printByteCode,
                logEnabled,
                run,
                true,
                lc,
                new JelikClassLoader()
        );

        return new JelikCompileSession(config);
    }

    public @NotNull List<ToByteCodeResult> compile(@NotNull final JelikCompileSession session) {
        var start = System.nanoTime();

        //jelik and java files
        //TODO java files
        final Pair<List<File>, List<File>> filesToCompile = Pair.of(
                session.getConfig().getFilesToCompile().stream().map(File::new).collect(Collectors.toList()),
                Collections.emptyList()
        );

        if (session.isLogEnabled()) {
            log.info("Found {} jelik files to compile", filesToCompile.getLeft().size());
            log.info("Found {} java files to compile", filesToCompile.getRight().size());
        }

        System.setProperty("compilerExitOnError", "true");

        try {
            //compiles given jelik files
            var result = compileFiles(session, filesToCompile.getLeft(), filesToCompile.getRight());
            long stop = System.nanoTime();
            if (session.isLogEnabled()) {
                log.info("Compilation finished in: {}", String.format("%.03f seconds", ((double) (stop-start) / 1000000000.0)));
            }
            return result;
        } catch (CompileException e) {
            ExceptionQueue.enqueue(e);
            if (session.isPrintCompilerException()) {
                throw e;
            }
            return Collections.emptyList();
        } finally {
            if (ExceptionQueue.hasExceptions()) {
                ExceptionQueue.printExceptions();
            }
        }
    }

    /**
     * Finds jelik and java files in given root directory
     *
     * @param dir source root directory to search
     * @return a Pair of lists of jelik and java source files
     */
    private static Pair<List<File>, List<File>> findFilesToCompile(@NotNull final File dir) {
        final Pair<List<File>, List<File>> files = Pair.of(new LinkedList<>(), new LinkedList<>());
        findFilesToCompile(files, dir);
        return files;
    }

    /**
     * Finds jelik and java files in given root directory
     *
     * @param acc accumulator for found jelik and java source files
     * @param dir root directory
     */
    private static void findFilesToCompile(@NotNull final Pair<List<File>, List<File>> acc, @NotNull final File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (final File f: files) {
            if (f.isFile()) {
                if (FilenameUtils.isExtension(f.getName(), "jlk")) {
                    acc.getLeft().add(f);
                }
                if (FilenameUtils.isExtension(f.getName(), "java")) {
                    acc.getRight().add(f);
                }
            } else {
                findFilesToCompile(acc, f);
            }
        }
    }

    //TODO parse java files
    static @NotNull List<ToByteCodeResult> compileFiles(@NotNull final JelikCompileSession session,
                                               @NotNull final List<File> jelikFiles,
                                               @NotNull final List<File> javaFiles) {
        final List<ModuleDeclaration> modules = new ArrayList<>();
        for (File f : jelikFiles) {
            final ModuleParser parser = new ModuleParser(f);
            final ParseContext parseContext = new ParseContext(f);
            modules.add(parser.visit(parseContext));
        }
        final CompilationContext compilationContext = new CompilationContext(session);
        return JelikCompiler.INSTANCE.compileModules(modules, compilationContext);
    }

    private static void validateArgs(@NotNull CommandLine args) {
        if (args.getArgList().isEmpty()) {
            System.err.println("Expected file to compile");
            System.exit(1);
        }

        for (String s : args.getArgList()) {
            final File file = new File(s);
            if (!FilenameUtils.isExtension(file.getName(), "jlk")) {
                System.err.println(file.getName() + " must be a Jelik file (*.jlk)");
                System.exit(1);
            }
            if (!file.exists()) {
                System.err.println(s + " does not exist");
                System.exit(1);
            }
        }
    }
}
