package org.jelik.parser.ast.resolvers.types;

import org.jelik.compiler.config.CompilationContext;
import org.jelik.parser.ast.classes.ClassDeclaration;
import org.jelik.parser.ast.types.CovariantTypeNode;
import org.jelik.parser.ast.types.JelikGenericType;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.types.CovariantType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class TypeParametersResolver extends TypeResolver {

    @Override
    public void visitClassDeclaration(@NotNull ClassDeclaration classDeclaration, @NotNull CompilationContext compilationContext) {
        compilationContext.pushCompilationUnit(classDeclaration);
        classDeclaration.getTypeParameterListNode().getTypes().forEach(t -> t.accept(this, compilationContext));
        compilationContext.popCompilationUnit();
    }

    @Override
    public void visitCovariantTypeNode(@NotNull CovariantTypeNode covariantTypeNode,
                                       @NotNull CompilationContext compilationContext) {

        super.visitCovariantTypeNode(covariantTypeNode, compilationContext);
        compilationContext.currentCompilationUnit()
                .addTypeParameterMapping(covariantTypeNode.getTypeNode().getSymbol(),
                        covariantTypeNode.getParentTypeNode());
        compilationContext.currentCompilationUnit()
                .addGenericTypeParameterMapping(covariantTypeNode.getTypeNode().getSymbol(), covariantTypeNode);

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
            typeNode.setType(new JelikGenericType(typeNode.getSymbol()));
            typeNode.setGenericType(new JelikGenericType(typeNode.getSymbol()));

            compilationContext.currentCompilationUnit()
                    .addTypeParameterMapping(typeNode.getSymbol(), typeNode);
            compilationContext.currentCompilationUnit()
                    .addGenericTypeParameterMapping(typeNode.getSymbol(), typeNode);
        }
    }
}
