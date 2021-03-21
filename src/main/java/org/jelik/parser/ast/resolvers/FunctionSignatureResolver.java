package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.functions.DefaultConstructorDeclaration;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.ast.functions.FunctionReturn;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeNodeRef;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionSignatureResolver extends AstVisitor {

    @Override
    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(classDeclaration);
        for (FunctionDeclaration functionDeclaration : classDeclaration.getFunctionDeclarations()) {
            functionDeclaration.visit(this, compilationContext);
        }
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitDefaultConstructor(@NotNull DefaultConstructorDeclaration defaultConstructorDeclaration, @NotNull CompilationContext compilationContext) {
        final Type owner = defaultConstructorDeclaration.getOwner();
        defaultConstructorDeclaration.getFunctionContext().setSignature("()" + owner.getInternalName());
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(functionDeclaration);

        functionDeclaration.getGenerics().forEach(g -> g.visit(new TypeParametersResolver(), compilationContext));

        FunctionTypeParametersResolver.INSTANCE.resolve(functionDeclaration, compilationContext);
        functionDeclaration.getFunctionParameterList().visit(this, compilationContext);
        functionDeclaration.getFunctionReturn().ifPresent(fr -> fr.visit(this, compilationContext));

        StringBuilder sb = new StringBuilder("(");

        for (FunctionParameter functionParameter : functionDeclaration.getFunctionParameterList().getFunctionParameters()) {
            sb.append(functionParameter.getTypeNode().getType().getDescriptor());
        }

        sb.append(")");

        functionDeclaration.getFunctionReturn().ifPresentOrElse(rt -> sb.append(rt.getTypeNode().getType().getDescriptor()), () -> sb.append("V"));

        functionDeclaration.getFunctionContext().setSignature(sb.toString());

        int localIndex = 0;
        if (!functionDeclaration.isStatic()) {

        }

        for (FunctionParameter functionParameter : functionDeclaration.getFunctionParameterList().getFunctionParameters()) {
            LocalVariable localVariable = new LocalVariable(functionParameter.getName().getText(), new TypeNodeRef(functionParameter.getTypeNode()), true);
            localVariable.index = localIndex;
            functionParameter.localVariableRef = localVariable;
            functionDeclaration.getFunctionContext().addLocalVariable(localVariable);
            localIndex++;
            if (localVariable.isDouble() || localVariable.isLong()) {
                localIndex++;
            }
        }

        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitFunctionParameterList(@NotNull FunctionParameterList fpl, @NotNull CompilationContext compilationContext) {
        for (FunctionParameter functionParameter : fpl.getFunctionParameters()) {
            functionParameter.getTypeNode().visit(new TypeResolver(), compilationContext);
            if (functionParameter.getTypeNode().getType() == null) {
                functionParameter.visit(FunctionTypeParametersResolver.INSTANCE, compilationContext);
            }
        }
    }

    @Override
    public void visit(@NotNull FunctionReturn functionReturn, @NotNull CompilationContext compilationContext) {
        functionReturn.getTypeNode().visit(new TypeResolver(), compilationContext);
        if (functionReturn.getTypeNode().getType() == null) {
            final TypeNode typeNode = compilationContext.currentCompilationUnit()
                    .getTypeParametersMappings()
                    .get(functionReturn.getTypeNode().getSymbol());
            if (typeNode == null) {
                throw new CompileException("Unresolved type",
                        functionReturn.getTypeNode(),
                        compilationContext.getCurrentModule());
            } else {
                functionReturn.getTypeNode().setType(typeNode.getType());
                functionReturn
                        .getTypeNode()
                        .setGenericType(compilationContext
                                .currentCompilationUnit()
                                .getGenericTypeParametersMappings()
                                .get(functionReturn.getTypeNode().getSymbol()).getType());
            }
        }
    }
}
