package org.jelik.parser.ast.resolvers;

import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.exceptions.CompileException;
import org.jelik.compiler.functions.ExtensionFunctionRegister;
import org.jelik.compiler.locals.LocalVariable;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.functions.ConstructorDeclaration;
import org.jelik.parser.ast.functions.DefaultConstructorDeclaration;
import org.jelik.parser.ast.functions.ExtensionFunctionDeclarationImpl;
import org.jelik.parser.ast.functions.FunctionDeclaration;
import org.jelik.parser.ast.functions.FunctionParameter;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.ast.functions.FunctionReturn;
import org.jelik.parser.ast.resolvers.types.TypeParametersResolver;
import org.jelik.parser.ast.resolvers.types.TypeResolver;
import org.jelik.parser.ast.types.InferredTypeNode;
import org.jelik.parser.ast.types.MaybeTypeNode;
import org.jelik.parser.ast.types.TypeNode;
import org.jelik.parser.ast.types.TypeNodeRef;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionSignatureResolver extends AstVisitor {

    @Override
    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration,
                                      @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(classDeclaration);
        new TypeParametersResolver().visitClassDeclaration(classDeclaration, compilationContext);
        for (FunctionDeclaration functionDeclaration : classDeclaration.getFunctionDeclarations()) {
            functionDeclaration.accept(this, compilationContext);
        }
        for (ConstructorDeclaration constructorDeclaration : classDeclaration.getConstructorDeclarations()) {
            constructorDeclaration.accept(this, compilationContext);
        }
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitDefaultConstructor(@NotNull DefaultConstructorDeclaration defaultConstructorDeclaration,
                                        @NotNull CompilationContext compilationContext) {
    }

    @Override
    public void visitConstructorDeclaration(@NotNull ConstructorDeclaration constructorDeclaration,
                                            @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(constructorDeclaration);
        FunctionTypeParametersResolver.INSTANCE.visitFunctionDeclaration(constructorDeclaration, compilationContext);
        constructorDeclaration.getFunctionParameterList().accept(this, compilationContext);
        int localIndex = 0;
        for (var functionParameter : constructorDeclaration.getFunctionParameterList().getFunctionParameters()) {
            var localVariable = new LocalVariable(functionParameter.getName().getText(),
                    new TypeNodeRef(functionParameter.getTypeNode()), true);
            localVariable.index = localIndex;
            functionParameter.localVariableRef = localVariable;
            constructorDeclaration.getFunctionContext().addLocalVariable(localVariable);
            localIndex++;
            if (localVariable.isDouble() || localVariable.isLong()) {
                localIndex++;
            }
        }
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitExtFunction(@NotNull ExtensionFunctionDeclarationImpl extensionFunctionDeclaration,
                                 @NotNull CompilationContext compilationContext) {
        final TypeNode extOwner = extensionFunctionDeclaration.getExtOwnerNode();
        extOwner.accept(new TypeResolver(), compilationContext);
        ExtensionFunctionRegister.INSTANCE.insert(extOwner.getGenericType(), extensionFunctionDeclaration);
        extensionFunctionDeclaration.getFunctionContext().addLocalVariable(new LocalVariable("this", new TypeNodeRef(extOwner), true));
        super.visitExtFunction(extensionFunctionDeclaration, compilationContext);
    }

    @Override
    public void visitFunctionDeclaration(@NotNull FunctionDeclaration functionDeclaration,
                                         @NotNull CompilationContext compilationContext) {

        compilationContext.pushCompilationUnit(functionDeclaration);

        functionDeclaration.getGenerics().forEach(g -> g.accept(new TypeParametersResolver(), compilationContext));

        FunctionTypeParametersResolver.INSTANCE.visitFunctionDeclaration(functionDeclaration, compilationContext);
        functionDeclaration.getFunctionParameterList().accept(this, compilationContext);

        if (!functionDeclaration.getFunctionReturn().isVoid()) {
            functionDeclaration.getFunctionReturn().accept(this, compilationContext);
        }

        int localIndex = 0;
        if (!functionDeclaration.isStatic()) {

        }

        for (FunctionParameter functionParameter : functionDeclaration.getFunctionParameterList()
                .getFunctionParameters()) {
            LocalVariable localVariable = new LocalVariable(functionParameter.getName().getText(),
                    new TypeNodeRef(functionParameter.getTypeNode()), true);
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
    public void visitFunctionParameterList(@NotNull FunctionParameterList fpl,
                                           @NotNull CompilationContext compilationContext) {
        for (FunctionParameter functionParameter : fpl.getFunctionParameters()) {
            functionParameter.getTypeNode().accept(new TypeResolver(), compilationContext);
            if (functionParameter.getTypeNode().getType() == null) {
                functionParameter.accept(FunctionTypeParametersResolver.INSTANCE, compilationContext);
            }
        }
    }

    @Override
    public void visit(@NotNull FunctionReturn functionReturn, @NotNull CompilationContext compilationContext) {
        if (functionReturn.getTypeNode() instanceof InferredTypeNode) {
            return;
        }
        functionReturn.getTypeNode().accept(new TypeResolver(), compilationContext);
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
        } else {
            if (functionReturn.getTypeNode().isMaybe()) {
                ((MaybeTypeNode) functionReturn.getTypeNode()).liftToWrapper();
            }
        }
    }
}
