package org.jelik.parser.ast.classes;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.MethodDeclaration;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.Modifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class InterfaceDeclaration extends ClassDeclaration {

    public InterfaceDeclaration(final String absoluteFilePath,
                                final List<Modifier> modifiers,
                                final Token classKeyword,
                                final LiteralToken name,
                                final TypeVariableListNode typeParameterListNode,
                                final List<FunctionDeclaration> functionDeclarations,
                                final List<MethodDeclaration> methodDeclarations) {

        super(absoluteFilePath,
                modifiers,
                classKeyword,
                name,
                typeParameterListNode,
                Collections.emptyList(),
                functionDeclarations,
                methodDeclarations,
                Collections.emptyList());
    }

    @Override
    public boolean isAbstract() {
        return true;
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitInterfaceDeclaration(this, compilationContext);
    }
}
