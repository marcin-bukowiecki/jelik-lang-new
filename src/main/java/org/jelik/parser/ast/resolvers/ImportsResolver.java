package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.JelikCompiler;
import org.jelik.parser.ast.classes.ModuleDeclaration;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.ImportDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class ImportsResolver extends AstVisitor {

    @Override
    public void visitModuleDeclaration(@NotNull ModuleDeclaration moduleDeclaration, @NotNull CompilationContext compilationContext) {
        moduleDeclaration.getImports().forEach(i -> i.accept(this, compilationContext));
    }

    @Override
    public void visitImportDeclaration(@NotNull ImportDeclaration importDeclaration, @NotNull CompilationContext compilationContext) {
        String canonicalPath = importDeclaration.canonicalPath();
        JelikCompiler.INSTANCE.findClassData(canonicalPath, compilationContext).ifPresentOrElse(aClass -> {
            //JavaType javaType = new JavaType(aClass);
            importDeclaration.setType(aClass.getType());
        }, () -> {

        });
    }
}
