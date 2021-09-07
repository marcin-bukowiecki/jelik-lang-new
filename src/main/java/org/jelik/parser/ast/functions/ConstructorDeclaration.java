package org.jelik.parser.ast.functions;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.EmptyToken;
import org.jelik.parser.token.Token;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class ConstructorDeclaration extends FunctionDeclaration {

    public ConstructorDeclaration(Token keyword,
                                  FunctionParameterList functionParameterList,
                                  FunctionBody functionBody) {
        super(keyword,
                EmptyToken.INSTANCE,
                functionParameterList,
                VoidFunctionReturn.INSTANCE,
                functionBody,
                TypeVariableListNode.Companion.getEMPTY());
    }

    @Override
    public @NotNull String getName() {
        return "<init>";
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitConstructorDeclaration(this, compilationContext);
    }

    public @NotNull String getDescriptor() {
        var acc = new StringBuilder("(");
        for (FunctionParameter functionParameter : getFunctionParameterList().getFunctionParameters()) {
            acc.append(functionParameter.getTypeNode().getType().getDescriptor());
        }
        acc.append(")V");
        return acc.toString();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public List<Type> getExpectedTypeParameters() {
        return ((ClassDeclaration) getParent())
                .getTypeParameterListNode()
                .getTypes().stream()
                .map(TypeNode::getType)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isConstructor() {
        return true;
    }

    @Override
    public Type getReturnType() {
        return JVMVoidType.INSTANCE;
    }

    @Override
    public Type getGenericReturnType() {
        return ((ClassDeclaration) getParent()).getType();
    }

    @Override
    public String toString() {
        return "" + getKeyword() + getFunctionParameterList() + getFunctionBody();
    }
}
