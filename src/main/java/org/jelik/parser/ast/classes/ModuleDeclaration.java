package org.jelik.parser.ast.classes;

import org.jelik.CompilationContext;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.PackageDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleDeclaration extends ClassDeclaration implements CompilationUnit, ClassData {

    private final List<ClassDeclaration> classes;

    private final List<ImportDeclaration> imports;

    private final PackageDeclaration packageDeclaration;

    public ModuleDeclaration(String name,
                             String absolutePath,
                             List<ImportDeclaration> importDeclarations,
                             List<FunctionDeclaration> functionDeclarations,
                             List<ClassDeclaration> classes,
                             PackageDeclaration packageDeclaration) {
        super(absolutePath, new LiteralToken(-1, -1, name), functionDeclarations, Collections.emptyList());
        for (ImportDeclaration importDeclaration : importDeclarations) {
            importDeclaration.parent = this;
        }
        this.imports = importDeclarations;
        this.classes = classes;
        this.packageDeclaration = packageDeclaration;
        this.packageDeclaration.setParent(this);
        this.classes.forEach(c -> c.setParent(this));
    }

    public PackageDeclaration getPackageDeclaration() {
        return packageDeclaration;
    }

    public List<ImportDeclaration> getImports() {
        return imports;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitModuleDeclaration(this, compilationContext);
    }

    @Override
    public ModuleDeclaration getModuleDeclaration() {
        return this;
    }

    public List<ClassDeclaration> getClasses() {
        return Collections.unmodifiableList(classes);
    }
}
