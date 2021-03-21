package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class ImportedTypeResolver extends AstVisitor {

    @Override
    public void visitSingleTypeNode(@NotNull SingleTypeNode typeNode, @NotNull CompilationContext compilationContext) {
        ClassDeclaration currentModule = compilationContext.getCurrentModule();
        List<ImportDeclaration> importDeclarations = currentModule.getModuleDeclaration().getImports();
        for (ImportDeclaration importDeclaration : importDeclarations) {
            Type typeInImport = findTypeInImport(typeNode, importDeclaration);
            if (typeInImport != null) {
                typeNode.setType(typeInImport);
                typeNode.setGenericType(typeInImport.deepGenericCopy());
                break;
            }
        }
    }

    @Override
    public void visitImportDeclaration(@NotNull ImportDeclaration importDeclaration, @NotNull CompilationContext compilationContext) {
        new TypeResolver().visitImportDeclaration(importDeclaration, compilationContext);
    }

    private static Type findTypeInImport(SingleTypeNode singleTypeNode, ImportDeclaration importDeclaration) {
        if (importDeclaration.getType().getCanonicalName().endsWith(singleTypeNode.getText())) {
            return importDeclaration.getType();
        }
        return null;
    }
}
