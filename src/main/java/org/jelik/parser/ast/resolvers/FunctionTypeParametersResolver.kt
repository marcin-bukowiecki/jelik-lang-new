package org.jelik.parser.ast.resolvers

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.exceptions.CompileException
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.functions.FunctionParameter
import org.jelik.parser.ast.functions.FunctionReturn
import org.jelik.parser.ast.types.CovariantTypeNode
import org.jelik.parser.ast.types.SingleTypeNode
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * Checks if given type is a function generic or class generic
 *
 * i.e.
 *
 * fun expr<T>(foo T) {
 *
 * }
 *
 * will resolve T as generic type
 *
 * if:
 *
 * fun expr(foo T) {
 *
 * }
 *
 * Will check also the owning class for T type. If this visitor won't find type anywhere it will throw exception
 *
 * @author Marcin Bukowiecki
 */
object FunctionTypeParametersResolver : AstVisitor() {

    override fun visitFunctionDeclaration(functionDeclaration: FunctionDeclaration,
                                          compilationContext: CompilationContext
    ) {

        val generics = functionDeclaration.generics
        generics.forEach { it.accept(this, compilationContext) }
    }

    override fun visitCovariantTypeNode(covariantTypeNode: CovariantTypeNode,
                                        compilationContext: CompilationContext
    ) {

        covariantTypeNode.parentTypeNode.accept(this, compilationContext)
        compilationContext
                .currentFunction()
                .functionContext
                .addTypeParameterMapping(covariantTypeNode.typeNode.symbol, covariantTypeNode.parentTypeNode)
    }

    override fun visitSingleTypeNode(t: SingleTypeNode, compilationContext: CompilationContext) {

    }

    override fun visit(fr: FunctionReturn, compilationContext: CompilationContext) {
        val typeNode = compilationContext.currentFunction()
                .functionContext.typeParametersMappings[fr.typeNode.symbol] ?: throw CompileException("Unresolved type",
                fr.typeNode,
                compilationContext.currentModule)

        fr.typeNode.type = typeNode.type
        fr.typeNode.genericType = typeNode.genericType
    }

    override fun visit(fp: FunctionParameter, compilationContext: CompilationContext) {
        val typeNode = compilationContext.currentFunction().functionContext.typeParametersMappings[fp.typeNode.symbol]
                ?: throw CompileException("Unresolved type ${fp.typeNode}",
                        fp.typeNode,
                        compilationContext.currentModule)
        fp.typeNode.type = typeNode.type
        fp.typeNode.genericType = typeNode.genericType
    }
}
