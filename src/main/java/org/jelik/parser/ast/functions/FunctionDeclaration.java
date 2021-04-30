package org.jelik.parser.ast.functions;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.compiler.data.MethodData;
import org.jelik.compiler.helper.LocalVariableFinder;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.ASTNodeImpl;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.resolvers.types.TypeAccessSymbolResult;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionDeclaration extends ASTNodeImpl implements CompilationUnit, MethodData {

    protected final Token keyword;

    protected final LiteralToken name;

    protected final FunctionParameterList functionParameterList;

    protected final FunctionReturn functionReturn;

    protected final FunctionBody functionBody;

    protected final FunctionContext functionContext = new FunctionContext(this);

    protected final TypeVariableListNode typeParameterListNode;

    public FunctionDeclaration(final @NotNull Token keyword,
                               final @NotNull LiteralToken name,
                               final @NotNull FunctionParameterList functionParameterList,
                               final @NotNull FunctionReturn functionReturn,
                               final @NotNull FunctionBody functionBody,
                               final @NotNull TypeVariableListNode typeParameterListNode) {

        this.keyword = keyword;
        this.name = name;
        this.typeParameterListNode = typeParameterListNode;
        this.functionParameterList = functionParameterList;
        this.functionReturn = functionReturn;
        this.functionBody = functionBody;
        functionBody.setParent(this);
    }

    public @NotNull FunctionContext getFunctionContext() {
        return functionContext;
    }

    public @NotNull List<@NotNull TypeNode> getGenerics() {
        return Collections.unmodifiableList(typeParameterListNode.getTypes());
    }

    public @NotNull Token getKeyword() {
        return keyword;
    }

    public @NotNull String getName() {
        return name.getText();
    }

    public @NotNull FunctionParameterList getFunctionParameterList() {
        return functionParameterList;
    }

    public FunctionReturn getFunctionReturn() {
        return functionReturn;
    }

    public @NotNull FunctionBody getFunctionBody() {
        return functionBody;
    }

    @Override
    public String toString() {
        return keyword + " " +
                getName() +
                functionParameterList + " " +
                (functionReturn == null ? "" : functionReturn.toString()) +
                functionBody;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitFunctionDeclaration(this, compilationContext);
    }

    @Override
    public Optional<FindSymbolResult> findSymbol(String text, CompilationContext compilationContext) {
        Optional<LocalVariable> local = LocalVariableFinder.INSTANCE.tryFindLocalVariable(text, compilationContext);
        if (local.isPresent()) {
            return Optional.of(local.get().toFindSymbolResult());
        }
        final TypeNode typeNode = functionContext.getTypeParametersMappings().get(text);
        if (typeNode != null) {
            return Optional.of(new TypeAccessSymbolResult(typeNode.getType()));
        }
        return ((CompilationUnit) getParent()).findSymbol(text, compilationContext);
    }

    public @NotNull String getDescriptor() {
        var acc = new StringBuilder("(");
        for (FunctionParameter functionParameter : getFunctionParameterList().getFunctionParameters()) {
            acc.append(functionParameter.getTypeNode().getType().getDescriptor());
        }
        acc.append(")");
        if (functionReturn.isVoid()) {
            acc.append("V");
        } else {
            acc.append(functionReturn.getTypeNode().getType().getDescriptor());
        }
        return acc.toString();
    }

    @Override
    public @NotNull List<Type> getParameterTypes() {
        return functionParameterList.getFunctionParameters().stream()
                .map(p -> p.getTypeNode().getType())
                .collect(Collectors.toList());
    }

    @Override
    public Type getReturnType() {
        if (functionReturn.isVoid()) return JVMVoidType.INSTANCE;
        return functionReturn.getTypeNode().getType();
    }

    @Override
    public Type getGenericReturnType() {
        if (functionReturn.isVoid()) return JVMVoidType.INSTANCE;
        return functionReturn.getTypeNode().getGenericType();
    }

    @Override
    public List<Type> getGenericParameterTypes() {
        return functionParameterList.getFunctionParameters().stream()
                .map(p -> p.getTypeNode().getGenericType())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isConstructor() {
        return false;
    }

    public String getSignature() {
        return null;
    }

    public String[] getExceptions() {
        return new String[0];
    }

    public List<LocalVariable> getLocalVariables() {
        return functionContext.getLocalVariableList();
    }

    public boolean isStatic() {
        return true;
    }

    @Override
    public Type getOwner() {
        return ((ClassDeclaration) getParent()).getType();
    }

    @Override
    public List<Type> getExpectedTypeParameters() {
        return typeParameterListNode.getTypes().stream().map(TypeNode::getType).collect(Collectors.toList());
    }

    @Override
    public Optional<Type> findType(SingleTypeNode typeNode, CompilationContext compilationContext) {
        Objects.requireNonNull(getParent());
        return ((ClassDeclaration) getParent()).findType(typeNode, compilationContext);
    }

    @Override
    public Map<String, TypeNode> getTypeParametersMappings() {
        return functionContext.getTypeParametersMappings();
    }

    @Override
    public void addTypeParameterMapping(String symbol, TypeNode typeNode) {
        functionContext.addTypeParameterMapping(symbol, typeNode);
    }

    @Override
    public Map<String, TypeNode> getGenericTypeParametersMappings() {
        return functionContext.getGenericTypeParametersMappings();
    }

    @Override
    public void addGenericTypeParameterMapping(String symbol, TypeNode typeNode) {
        functionContext.addGenericTypeParameterMapping(symbol, typeNode);
    }

    public String getText() {
        return keyword.getText() + " " +
                name.getText() +
                typeParameterListNode.toString() +
                functionParameterList.toString() +
                (functionReturn == null ? " " : " " + functionReturn) +
                functionBody.toString();
    }
}
