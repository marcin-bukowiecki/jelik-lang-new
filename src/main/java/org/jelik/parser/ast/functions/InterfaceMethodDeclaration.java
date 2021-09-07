package org.jelik.parser.ast.functions;

import org.jelik.compiler.CompilationContext;
import org.jelik.parser.ast.types.TypeVariableListNode;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class InterfaceMethodDeclaration extends MethodDeclaration {

    public InterfaceMethodDeclaration(Token keyword,
                                      LiteralToken name,
                                      FunctionParameterList functionParameterList,
                                      FunctionReturn functionReturn,
                                      TypeVariableListNode typeParameterListNode) {
        super(keyword, name, functionParameterList, functionReturn, FunctionBodyBlock.createEmpty(), typeParameterListNode);
    }

    @Override
    public void accept(@NotNull AstVisitor astVisitor, @NotNull CompilationContext compilationContext) {
        astVisitor.visitInterfaceMethodDeclaration(this, compilationContext);
    }
}
