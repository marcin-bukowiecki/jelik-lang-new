package org.jelik.parser.ast.classes;

import org.jelik.CompilationContext;
import org.jelik.compiler.common.TypeEnum;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.data.FieldData;
import org.jelik.compiler.data.MethodData;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.ModuleContext;
import org.jelik.parser.ast.functions.ConstructorDeclaration;
import org.jelik.parser.ast.functions.DefaultConstructorDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.resolvers.BuiltinTypeRegister;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.resolvers.FunctionReferenceResult;
import org.jelik.parser.ast.resolvers.TypeAccessSymbolResult;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.keyword.ClassKeyword;
import org.jelik.types.JVMObjectType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class ClassDeclaration extends ASTNode implements CompilationUnit, ClassData {

    private final ClassKeyword classKeyword;

    private final LiteralToken name;

    public final ModuleContext moduleContext = new ModuleContext();

    private final List<FieldDeclaration> fieldDeclarations;

    private final List<FunctionDeclaration> functionDeclarations;

    private final List<ConstructorDeclaration> constructorDeclarations;

    public ClassDeclaration(final String absoluteFilePath,
                            final ClassKeyword classKeyword,
                            final LiteralToken name,
                            final List<FieldDeclaration> fieldDeclarations,
                            final List<FunctionDeclaration> functionDeclarations,
                            final List<ConstructorDeclaration> constructorDeclarations) {

        this.classKeyword = classKeyword;
        this.name = name;
        this.moduleContext.setFileAbsolutePath(absoluteFilePath);
        this.fieldDeclarations = fieldDeclarations;
        this.functionDeclarations = functionDeclarations;
        this.functionDeclarations.forEach(f -> f.setParent(this));
        if (constructorDeclarations.isEmpty()) {
            this.constructorDeclarations = Collections.singletonList(new DefaultConstructorDeclaration());
        } else {
            this.constructorDeclarations = constructorDeclarations;
        }
        this.constructorDeclarations.forEach(c -> c.setParent(this));
    }

    public List<ConstructorDeclaration> getConstructorDeclarations() {
        return Collections.unmodifiableList(constructorDeclarations);
    }

    public ModuleDeclaration getModuleDeclaration() {
        return ((ModuleDeclaration) parent);
    }

    public String getAbsoluteFilePath() {
        return moduleContext.getFileAbsolutePath();
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        return Collections.unmodifiableList(functionDeclarations);
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitClassDeclaration(this, compilationContext);
    }

    public String getCanonicalName() {
        return getModuleDeclaration().getPackageDeclaration().isDefault() ?
                name.getText() :
                getModuleDeclaration().getPackageDeclaration().getPrettyPath() + name;
    }

    @Override
    public Optional<FindSymbolResult> findSymbol(String text, CompilationContext compilationContext) {
        for (ImportDeclaration importDeclaration : getModuleDeclaration().getImports()) {
            if (importDeclaration.getType().getName().equals(text)) {
                return Optional.of(new TypeAccessSymbolResult(importDeclaration.getType()));
            }
        }

        final List<? extends MethodData> byName = findByName(text, compilationContext);
        if (!byName.isEmpty()) {
            return Optional.of(new FunctionReferenceResult(byName));
        }

        return Optional.ofNullable(BuiltinTypeRegister.INSTANCE.checkForBuiltinByName(text)
                .map(TypeAccessSymbolResult::new)
                .orElse(DefaultImportedTypeResolver.getTypeOpt(text).map(TypeAccessSymbolResult::new).orElse(null)));
    }

    @Override
    public List<? extends MethodData> findByName(String name, CompilationContext compilationContext) {
        return functionDeclarations.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public List<FieldData> findFieldByName(String name) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasSuperClass() {
        return false;
    }

    @Override
    public List<Type> getInterfaceTypes() {
        return Collections.emptyList();
    }

    @Override
    public Type getParentType() {
        return JVMObjectType.INSTANCE;
    }

    public Type getType() {
        return new Type(name.getText(), getCanonicalName(), TypeEnum.objectT);
    }

    public String getSimpleName() {
        return name.getText();
    }

    @Override
    public Optional<Type> findType(SingleTypeNode typeNode, CompilationContext compilationContext) {
        for (ClassDeclaration aClass : this.getModuleDeclaration().getClasses()) {
            if (aClass.typeMatches(typeNode)) {
                return Optional.of(aClass.getType());
            }
        }
        return Optional.empty();
    }

    @Override
    public Map<String, TypeNode> getTypeParametersMappings() {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, TypeNode> getGenericTypeParametersMappings() {
        return Collections.emptyMap();
    }

    public List<FieldDeclaration> getFieldDeclarations() {
        return Collections.unmodifiableList(fieldDeclarations);
    }

    private boolean typeMatches(SingleTypeNode typeNode) {
        return typeNode.getText().equals(getSimpleName());
    }

    @Override
    public String toString() {
        return this.classKeyword + " " + this.name.getText();
    }
}
