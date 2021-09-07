package org.jelik.parser.ast.classes;

import org.jelik.compiler.runtime.TypeEnum;
import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.data.ClassData;
import org.jelik.compiler.data.FieldData;
import org.jelik.compiler.data.MethodData;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.ImportDeclaration;
import org.jelik.parser.ast.ModuleContext;
import org.jelik.parser.ast.functions.ConstructorDeclaration;
import org.jelik.parser.ast.functions.DefaultConstructorDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.MethodDeclaration;
import org.jelik.parser.ast.resolvers.BuiltinTypeRegister;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.resolvers.FunctionReferenceResult;
import org.jelik.parser.ast.resolvers.types.TypeAccessSymbolResult;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.utils.ASTDataKey;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.AbstractKeyword;
import org.jelik.parser.token.keyword.Modifier;
import org.jelik.types.JVMObjectType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marcin Bukowiecki
 */
public class ClassDeclaration extends ASTNodeImpl implements CompilationUnit, ClassData {

    public final ModuleContext moduleContext = new ModuleContext();

    private final Token keyword;

    private final LiteralToken name;

    private final TypeVariableListNode typeParameterListNode;

    private final List<FieldDeclaration> fieldDeclarations;

    private final List<FunctionDeclaration> functionDeclarations;

    private final List<MethodDeclaration> methodDeclarations;

    private final List<ConstructorDeclaration> constructorDeclarations;

    private final ClassContext classContext = new ClassContext(this);

    private final List<Modifier> modifiers;

    public ClassDeclaration(final String absoluteFilePath,
                            final List<Modifier> modifiers,
                            final Token keyword,
                            final LiteralToken name,
                            final TypeVariableListNode typeParameterListNode,
                            final List<FieldDeclaration> fieldDeclarations,
                            final List<FunctionDeclaration> functionDeclarations,
                            final List<MethodDeclaration> methodDeclarations,
                            final List<ConstructorDeclaration> constructorDeclarations) {

        this.modifiers = modifiers;
        this.keyword = keyword;
        this.name = name;
        this.typeParameterListNode = typeParameterListNode;
        this.moduleContext.setFileAbsolutePath(absoluteFilePath);
        this.fieldDeclarations = fieldDeclarations;
        this.functionDeclarations = functionDeclarations;
        this.methodDeclarations = methodDeclarations;
        this.functionDeclarations.forEach(f -> f.setParent(this));
        this.methodDeclarations.forEach(f -> f.setParent(this));
        if (constructorDeclarations.isEmpty()) {
            this.constructorDeclarations = Collections.singletonList(new DefaultConstructorDeclaration());
        } else {
            this.constructorDeclarations = constructorDeclarations;
        }
        this.constructorDeclarations.forEach(c -> c.setParent(this));
        this.dataHolder.putData(ASTDataKey.IS_ABSTRACT, modifiers.stream().anyMatch(m -> m instanceof AbstractKeyword));
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public String getSuperInternalName() {
        return "java/lang/Object";
    }

    public boolean isAbstract() {
        return Boolean.TRUE.equals(this.dataHolder.getData(ASTDataKey.IS_ABSTRACT));
    }

    public TypeVariableListNode getTypeParameterListNode() {
        return typeParameterListNode;
    }

    public List<ConstructorDeclaration> getConstructorDeclarations() {
        return Collections.unmodifiableList(constructorDeclarations);
    }

    public ModuleDeclaration getModuleDeclaration() {
        return ((ModuleDeclaration) getParent());
    }

    public String getAbsoluteFilePath() {
        return moduleContext.getFileAbsolutePath();
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        return Collections.unmodifiableList(functionDeclarations);
    }

    public List<MethodDeclaration> getMethodDeclarations() {
        return Collections.unmodifiableList(methodDeclarations);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitClassDeclaration(this, compilationContext);
    }

    public String getCanonicalName() {
        return getModuleDeclaration().getPackageDeclaration().isDefault() ?
                name.getText() :
                getModuleDeclaration().getPackageDeclaration().getPrettyPath() + "." + name;
    }

    public String getCanonicalNameForByteCode() {
        return getCanonicalName().replace('.', '/');
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
    public @NotNull List<? extends MethodData> findByName(String name, CompilationContext compilationContext) {
        return Stream.concat(
                functionDeclarations.stream().filter(m -> m.getName().equals(name)),
                constructorDeclarations.stream().filter(c -> c.getName().equals(name))
        ).collect(Collectors.toList());
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
    public List<MethodData> getMethodScope() {
        return functionDeclarations.stream().map(f -> ((MethodData) f)).collect(Collectors.toList());
    }

    @Override
    public Type getParentType() {
        return JVMObjectType.INSTANCE;
    }

    public Type getType() {
        return new Type(name.getText(),
                getCanonicalName(),
                TypeEnum.objectT,
                typeParameterListNode.getTypes().stream().map(t -> t.getType().deepGenericCopy()).collect(Collectors.toList()),
                typeParameterListNode.getTypes().stream().map(t -> t.getType().deepGenericCopy()).collect(Collectors.toList())
        );
    }

    @Override
    public Collection<? extends MethodData> getConstructorScope() {
        return constructorDeclarations;
    }

    @Override
    public boolean isInterface() {
        return false;
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
        return classContext.getTypeParametersMappings();
    }

    @Override
    public void addTypeParameterMapping(String symbol, TypeNode typeNode) {
        classContext.addTypeParameterMapping(symbol, typeNode);
    }

    @Override
    public Map<String, TypeNode> getGenericTypeParametersMappings() {
        return classContext.getGenericTypeParametersMappings();
    }

    @Override
    public void addGenericTypeParameterMapping(String symbol, TypeNode typeNode) {
        classContext.addGenericTypeParameterMapping(symbol, typeNode);
    }

    public List<FieldDeclaration> getFieldDeclarations() {
        return Collections.unmodifiableList(fieldDeclarations);
    }

    private boolean typeMatches(SingleTypeNode typeNode) {
        return typeNode.getText().equals(getSimpleName());
    }

    @Override
    public String toString() {
        return this.keyword + " " + this.name.getText() +
                (this.typeParameterListNode ==
                        TypeVariableListNode.Companion.getEMPTY() ? "" :
                        this.typeParameterListNode.toString()) + "{\n" +
                this.constructorDeclarations.stream()
                .map(ConstructorDeclaration::toString)
                .collect(Collectors.joining()) + "\n}";
    }

    public boolean hasMain() {
        return functionDeclarations.stream().anyMatch(FunctionDeclaration::isMain);
    }
}
