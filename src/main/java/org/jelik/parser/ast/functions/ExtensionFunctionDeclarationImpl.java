package org.jelik.parser.ast.functions;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Marcin Bukowiecki
 */
public class ExtensionFunctionDeclarationImpl extends FunctionDeclaration implements ExtensionFunctionDeclaration {

    private final Token extKeyword;

    private final TypeNode extOwner;

    public ExtensionFunctionDeclarationImpl(final Token extKeyword,
                                            final TypeNode extOwner,
                                            final Token keyword,
                                            final LiteralToken name,
                                            final FunctionParameterList functionParameterList,
                                            final FunctionReturn functionReturn,
                                            final FunctionBody functionBody,
                                            final TypeVariableListNode typeParameterListNode) {
        super(keyword, name, functionParameterList, functionReturn, functionBody, typeParameterListNode);
        this.extKeyword = extKeyword;
        this.extOwner = extOwner;
    }

    public @NotNull String getDescriptor() {
        var acc = new StringBuilder("(");
        acc.append(Objects.requireNonNull(getExtOwner()).getDescriptor());
        for (var functionParameter : getFunctionParameterList().getFunctionParameters()) {
            acc.append(functionParameter.getTypeNode().getType().getDescriptor());
        }
        acc.append(")");
        acc.append(getFunctionReturn().getTypeNode().getType().getDescriptor());
        return acc.toString();
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitExtFunction(this, compilationContext);
    }

    public @NotNull TypeNode getExtOwnerNode() {
        return extOwner;
    }

    public @Nullable Type getExtOwner() {
        return extOwner.getType();
    }

    @Override
    public @Nullable Type getGenericExtOwner() {
        return extOwner.getGenericType();
    }

    public String getText() {
        return extKeyword.getText() + " " +
                keyword.getText() + " " +
                extOwner + " " +
                name.getText() +
                typeParameterListNode.getText() +
                functionParameterList.toString() +
                (functionReturn == null ? " " : " " + functionReturn) +
                functionBody.toString();
    }

    @Override
    public boolean isExt() {
        return true;
    }
}
