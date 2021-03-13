package org.jelik.parser.ast.resolvers;

import org.apache.commons.io.FilenameUtils;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleSignatureResolver extends AstVisitor {

    @Override
    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration, @NotNull CompilationContext compilationContext) {
        String fileName = FilenameUtils.getBaseName(classDeclaration.moduleContext.getFileAbsolutePath());
        if (classDeclaration.getModuleDeclaration().getPackageDeclaration() == null ||
                classDeclaration.getModuleDeclaration().getPackageDeclaration().isDefault()) {
            classDeclaration.moduleContext.setCanonicalName(fileName);
        } else {
            classDeclaration
                    .moduleContext
                    .setCanonicalName(classDeclaration.getModuleDeclaration().getPackageDeclaration().getPrettyPath() + "." + fileName);
        }
    }
}
