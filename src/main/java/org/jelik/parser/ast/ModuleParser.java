package org.jelik.parser.ast;

import org.apache.commons.io.FilenameUtils;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.visitors.ClassDeclarationVisitor;
import org.jelik.parser.ast.visitors.FunctionDeclarationVisitor;
import org.jelik.parser.ast.visitors.ImportVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.ClassKeyword;
import org.jelik.parser.token.keyword.FunKeyword;
import org.jelik.parser.token.NewLineToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ImportKeyword;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleParser implements ParseVisitor<ModuleDeclaration> {

    private final List<FunctionDeclaration> functionDeclarations = new ArrayList<>();

    private final List<ClassDeclaration> classDeclarations = new ArrayList<>();

    private final List<ImportDeclaration> imports = new ArrayList<>();

    private final String absoluteFilePath;

    public ModuleParser(String absoluteFilePath) {
        this.absoluteFilePath = absoluteFilePath;
    }

    public @NotNull ModuleDeclaration visit(final @NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        while (lexer.hasNextToken()) {
            Token nextToken = lexer.nextToken();
            nextToken.visit(this, parseContext);
        }
        String baseName = FilenameUtils.getBaseName(absoluteFilePath);
        if (classDeclarations.isEmpty()) {
            return new ModuleDeclaration(baseName,
                    absoluteFilePath,
                    imports,
                    functionDeclarations,
                    Collections.emptyList(),
                    DefaultPackageDeclaration.INSTANCE);
        } else {
            if (classDeclarations.stream().anyMatch(c -> c.getSimpleName().equals(baseName))) {
                return new ModuleDeclaration(baseName + "JlkModule", absoluteFilePath, imports,
                        functionDeclarations, classDeclarations, DefaultPackageDeclaration.INSTANCE);
            } else {
                return new ModuleDeclaration(baseName, absoluteFilePath, imports,
                        functionDeclarations, classDeclarations, DefaultPackageDeclaration.INSTANCE);
            }
        }
    }

    @Override
    public void visitNewLine(@NotNull NewLineToken newLineToken, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitFunKeyword(@NotNull FunKeyword funKeyword, @NotNull ParseContext parseContext) {
        functionDeclarations.add(new FunctionDeclarationVisitor(funKeyword).visit(parseContext));
    }

    @Override
    public void visitImportKeyword(@NotNull ImportKeyword importKeyword, @NotNull ParseContext parseContext) {
        imports.add(new ImportVisitor(importKeyword).visit(parseContext));
    }

    @Override
    public void visitClassKeyword(@NotNull ClassKeyword classKeyword, @NotNull ParseContext parseContext) {
        classDeclarations.add(new ClassDeclarationVisitor(classKeyword).visit(parseContext));
        parseContext.getLexer().nextToken().visit(this, parseContext);
    }
}
