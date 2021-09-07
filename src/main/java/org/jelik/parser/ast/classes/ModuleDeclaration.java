package org.jelik.parser.ast.classes;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.PackageDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.ClassKeyword;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class ModuleDeclaration extends ClassDeclaration implements CompilationUnit, ClassData {

    private final List<ClassDeclaration> classes;

    private final List<InterfaceDeclaration> interfaces;

    private final List<ImportDeclaration> imports;

    private final PackageDeclaration packageDeclaration;

    public ModuleDeclaration(final String name,
                             final String absolutePath,
                             final List<ImportDeclaration> importDeclarations,
                             final List<FunctionDeclaration> functionDeclarations,
                             final List<ClassDeclaration> classes,
                             final List<InterfaceDeclaration> interfaces,
                             final PackageDeclaration packageDeclaration) {
        super(absolutePath,
                Collections.emptyList(),
                new ClassKeyword(-1),
                new LiteralToken(-1, name),
                TypeVariableListNode.Companion.getEMPTY(),
                Collections.emptyList(),
                functionDeclarations,
                Collections.emptyList(),
                Collections.emptyList());

        for (var importDeclaration : importDeclarations) {
            importDeclaration.setParent(this);
        }
        this.imports = importDeclarations;
        this.classes = classes;
        this.interfaces = interfaces;
        this.packageDeclaration = packageDeclaration;
        this.packageDeclaration.setParent(this);
        this.classes.forEach(c -> c.setParent(this));
        this.interfaces.forEach(i -> i.setParent(this));
    }

    @NotNull
    public PackageDeclaration getPackageDeclaration() {
        return packageDeclaration;
    }

    @NotNull
    public List<@NotNull ImportDeclaration> getImports() {
        return imports;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitModuleDeclaration(this, compilationContext);
    }

    @Override
    public ModuleDeclaration getModuleDeclaration() {
        return this;
    }

    @NotNull
    public List<@NotNull ClassDeclaration> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    @NotNull
    public List<@NotNull InterfaceDeclaration> getInterfaces() {
        return Collections.unmodifiableList(interfaces);
    }

    @Override
    public boolean isInterface() {
        return false;
    }
}
