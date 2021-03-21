package org.jelik.parser.ast.resolvers;

import org.jelik.CompilationContext;
import org.jelik.parser.ast.types.CovariantTypeNode;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.types.CovariantType;
import org.jelik.types.JVMObjectType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class TypeParametersResolver extends TypeResolver {

    @Override
    public void visitCovariantTypeNode(@NotNull CovariantTypeNode covariantTypeNode,
                                       @NotNull CompilationContext compilationContext) {

        super.visitCovariantTypeNode(covariantTypeNode, compilationContext);
        compilationContext.currentCompilationUnit()
                .getTypeParametersMappings()
                .put(covariantTypeNode.getTypeNode().getSymbol(), covariantTypeNode.getParentTypeNode());
        compilationContext.currentCompilationUnit()
                .getGenericTypeParametersMappings()
                .put(covariantTypeNode.getTypeNode().getSymbol(), covariantTypeNode);

        covariantTypeNode.setType(new CovariantType(covariantTypeNode.getSymbol(),
                covariantTypeNode.getParentTypeNode().getType()));
        covariantTypeNode.setGenericType(new CovariantType(covariantTypeNode.getSymbol(),
                covariantTypeNode.getParentTypeNode().getGenericType().deepGenericCopy()));
    }

    @Override
    public void visitSingleTypeNode(@NotNull SingleTypeNode typeNode,
                                    @NotNull CompilationContext compilationContext) {

        super.visitSingleTypeNode(typeNode, compilationContext);
        if (typeNode.getType() == null) {
            typeNode.setType(JVMObjectType.INSTANCE);
            typeNode.setGenericType(JVMObjectType.INSTANCE);
            compilationContext.currentCompilationUnit()
                    .getTypeParametersMappings()
                    .put(typeNode.getSymbol(), typeNode);
            compilationContext.currentCompilationUnit()
                    .getGenericTypeParametersMappings()
                    .put(typeNode.getSymbol(), typeNode);
        }
    }
}
