package org.jelik.parser.ast;

import org.apache.commons.io.FilenameUtils;
import org.jelik.compiler.utils.TokenUtils;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.InterfaceDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.LambdaDeclaration;
import org.jelik.parser.ast.visitors.ClassDeclarationVisitor;
import org.jelik.parser.ast.visitors.ImportVisitor;
import org.jelik.parser.ast.visitors.functions.ExtensionFunctionDeclarationVisitor;
import org.jelik.parser.ast.visitors.functions.FunctionDeclarationVisitor;
import org.jelik.parser.ast.visitors.interfaces.InterfaceDeclarationVisitor;
import org.jelik.parser.token.EofTok;
import org.jelik.parser.token.NewLineToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.AbstractKeyword;
import org.jelik.parser.token.keyword.ClassKeyword;
import org.jelik.parser.token.keyword.ExtKeyword;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jelik.parser.token.keyword.ImportKeyword;
import org.jelik.parser.token.keyword.InterfaceKeyword;
import org.jelik.parser.token.keyword.PackageKeyword;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleParser implements TokenVisitor<ModuleDeclaration> {

    private final List<FunctionDeclaration> functionDeclarations = new ArrayList<>();

    private final List<ClassDeclaration> classDeclarations = new ArrayList<>();

    private final List<InterfaceDeclaration> interfaceDeclarations = new ArrayList<>();

    private final List<ImportDeclaration> imports = new ArrayList<>();

    private final String absoluteFilePath;

    private PackageDeclaration packageDeclaration = DefaultPackageDeclaration.INSTANCE;

    public ModuleParser(String absoluteFilePath) {
        this.absoluteFilePath = absoluteFilePath;
    }

    public ModuleParser(File file) {
        this.absoluteFilePath = file.getAbsolutePath();
    }

    public @NotNull ModuleDeclaration visit(final @NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        while (lexer.hasNextToken()) {
            Token nextToken = lexer.nextToken();
            nextToken.accept(this, parseContext);
        }
        String baseName = FilenameUtils.getBaseName(absoluteFilePath);
        List<LambdaDeclaration> lambdaDeclarations = parseContext.lambdaDeclarations;
        parseContext.lambdaDeclarations = new ArrayList<>();
        if (!lambdaDeclarations.isEmpty()) {
            functionDeclarations.addAll(lambdaDeclarations);
        }
        if (classDeclarations.isEmpty() && interfaceDeclarations.isEmpty()) {
            return new ModuleDeclaration(baseName,
                    absoluteFilePath,
                    imports,
                    functionDeclarations,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    packageDeclaration);
        } else {
            if (classDeclarations.stream().anyMatch(c -> c.getSimpleName().equals(baseName)) ||
                    interfaceDeclarations.stream().anyMatch(c -> c.getSimpleName().equals(baseName))) {
                return new ModuleDeclaration(baseName + "JlkModule", absoluteFilePath, imports,
                        functionDeclarations, classDeclarations, interfaceDeclarations, packageDeclaration);
            } else {
                return new ModuleDeclaration(baseName, absoluteFilePath, imports,
                        functionDeclarations, classDeclarations, interfaceDeclarations, packageDeclaration);
            }
        }
    }

    @Override
    public void visitAbstractKeyword(@NotNull AbstractKeyword abstractKeyword, @NotNull ParseContext parseContext) {
        parseContext.modifiersStack.add(abstractKeyword);
    }

    @Override
    public void visitNewLine(@NotNull NewLineToken newLineToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitPackageKeyword(@NotNull PackageKeyword packageKeyword, @NotNull ParseContext parseContext) {
        packageDeclaration = new PackageVisitor(packageKeyword).visit(parseContext);
    }

    @Override
    public void visitFunKeyword(@NotNull FunKeyword funKeyword, @NotNull ParseContext parseContext) {
        functionDeclarations.add(new FunctionDeclarationVisitor(funKeyword).visit(parseContext));
    }

    @Override
    public void visitInterfaceKeyword(@NotNull InterfaceKeyword interfaceKeyword, @NotNull ParseContext parseContext) {
        interfaceDeclarations.add(new InterfaceDeclarationVisitor(interfaceKeyword).visit(parseContext));
        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visitExtKeyword(@NotNull ExtKeyword extKeyword, @NotNull ParseContext parseContext) {
        var next = parseContext.getLexer().nextToken();
        TokenUtils.checkCurrent("keyword.fun.expected", parseContext, FunKeyword.class);
        functionDeclarations.add(new ExtensionFunctionDeclarationVisitor(extKeyword, next).visit(parseContext));
    }

    @Override
    public void visitImportKeyword(@NotNull ImportKeyword importKeyword, @NotNull ParseContext parseContext) {
        imports.add(new ImportVisitor(importKeyword).visit(parseContext));
    }

    @Override
    public void visitClassKeyword(@NotNull ClassKeyword classKeyword, @NotNull ParseContext parseContext) {
        classDeclarations.add(new ClassDeclarationVisitor(classKeyword).visit(parseContext));
        parseContext.getLexer().nextToken().accept(this, parseContext);
    }

    @Override
    public void visit(EofTok eofTok, ParseContext parseContext) {

    }
}
