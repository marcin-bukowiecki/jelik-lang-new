package org.jelik.parser.ast.functions;

import org.jelik.CompilationContext;
import org.jelik.compiler.data.MethodData;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.compiler.model.CompilationUnit;
import org.jelik.parser.ast.ASTNode;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.resolvers.FindSymbolResult;
import org.jelik.parser.ast.resolvers.LocalVariableAccessSymbolResult;
import org.jelik.parser.ast.resolvers.TypeAccessSymbolResult;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionDeclaration extends ASTNode implements CompilationUnit, MethodData {

    private final Token keyword;

    private final LiteralToken name;

    private final FunctionParameterList functionParameterList;

    private final FunctionReturn functionReturn;

    private final FunctionBody functionBody;

    private final FunctionContext functionContext = new FunctionContext();

    private final List<TypeNode> generics;

    public FunctionDeclaration(Token keyword,
                               LiteralToken name,
                               FunctionParameterList functionParameterList,
                               FunctionReturn functionReturn,
                               FunctionBody functionBody,
                               List<TypeNode> generics) {

        this.keyword = keyword;
        this.name = name;
        this.generics = generics;
        this.functionParameterList = functionParameterList;
        this.functionReturn = functionReturn;
        this.functionBody = functionBody;
        functionBody.setParent(this);
    }

    public FunctionContext getFunctionContext() {
        return functionContext;
    }

    public List<TypeNode> getGenerics() {
        return Collections.unmodifiableList(generics);
    }

    public Token getKeyword() {
        return keyword;
    }

    public @NotNull String getName() {
        return name.getText();
    }

    public FunctionParameterList getFunctionParameterList() {
        return functionParameterList;
    }

    public Optional<FunctionReturn> getFunctionReturn() {
        return Optional.ofNullable(functionReturn);
    }

    public FunctionBody getFunctionBody() {
        return functionBody;
    }

    @Override
    public String toString() {
        return keyword + " " + name + functionParameterList + " " + functionReturn + functionBody;
    }

    @Override
    public void visit(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitFunctionDeclaration(this, compilationContext);
    }

    @Override
    public FindSymbolResult findSymbol(String text, CompilationContext compilationContext) {
        Optional<LocalVariable> local = compilationContext.findLocal(text);
        if (local.isPresent()) {
            return new LocalVariableAccessSymbolResult(local.get());
        }
        final TypeNode typeNode = functionContext.getGenericTypesMap().get(text);
        if (typeNode != null) {
            return new TypeAccessSymbolResult(typeNode.getType());
        }
        return ((CompilationUnit) getParent()).findSymbol(text, compilationContext);
    }

    public String getDescriptor() {
        var acc = new StringBuilder("(");
        for (FunctionParameter functionParameter : getFunctionParameterList().getFunctionParameters()) {
            acc.append(functionParameter.getTypeNode().getType().getDescriptor());
        }
        acc.append(")");
        acc.append(getFunctionReturn().map(rt -> rt.getTypeNode().getType().getDescriptor()).orElse("V"));
        return acc.toString();
    }

    @Override
    public List<Type> getParameterTypes() {
        return functionParameterList.getFunctionParameters().stream().map(p -> p.getTypeNode().getType()).collect(Collectors.toList());
    }

    @Override
    public Type getReturnType() {
        return functionReturn.getTypeNode().getType();
    }

    @Override
    public Type getGenericReturnType() {
        return functionReturn.getTypeNode().getGenericType();
    }

    @Override
    public List<Type> getGenericParameterTypes() {
        return functionParameterList.getFunctionParameters().stream().map(p -> p.getTypeNode().getGenericType()).collect(Collectors.toList());
    }

    public String getSignature() {
        return null;
    }

    public String[] getExceptions() {
        return new String[0];
    }

    public String getFunctionName() {
        return this.name.getText();
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
    public Optional<Type> findType(SingleTypeNode typeNode, CompilationContext compilationContext) {
        Objects.requireNonNull(parent);
        return ((ClassDeclaration) parent).findType(typeNode, compilationContext);
    }
}
