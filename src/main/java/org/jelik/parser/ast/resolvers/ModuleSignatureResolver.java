package org.jelik.parser.ast.resolvers;

import org.apache.commons.io.FilenameUtils;
import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.JelikCompiler;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleSignatureResolver extends AstVisitor {

    private final JelikCompiler jelikCompiler;

    public ModuleSignatureResolver(JelikCompiler jelikCompiler) {
        this.jelikCompiler = jelikCompiler;
    }

    @Override
    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration,
                                      @NotNull CompilationContext compilationContext) {

        jelikCompiler.classDataRegister.put(classDeclaration.getCanonicalName(), classDeclaration);
        String fileName = FilenameUtils.getBaseName(classDeclaration.moduleContext.getFileAbsolutePath());
        classDeclaration.getModuleDeclaration();
        if (classDeclaration.getModuleDeclaration().getPackageDeclaration().isDefault()) {
            classDeclaration.moduleContext.setCanonicalName(fileName);
        } else {
            classDeclaration
                    .moduleContext
                    .setCanonicalName(classDeclaration.getModuleDeclaration().getPackageDeclaration().getPrettyPath() + "." + fileName);
        }
    }
}
