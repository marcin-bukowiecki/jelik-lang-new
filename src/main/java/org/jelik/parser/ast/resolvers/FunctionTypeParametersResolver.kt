package org.jelik.parser.ast.resolvers

import org.jelik.CompilationContext
import org.jelik.compiler.exceptions.CompileException
import org.jelik.parser.ast.functions.FunctionDeclaration
import org.jelik.parser.ast.functions.FunctionParameter
import org.jelik.parser.ast.functions.FunctionReturn
import org.jelik.parser.ast.types.CovariantTypeNode
import org.jelik.parser.ast.types.SingleTypeNode
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
object FunctionTypeParametersResolver : AstVisitor() {

    fun resolve(functionDeclaration: FunctionDeclaration, compilationContext: CompilationContext) {
        val generics = functionDeclaration.generics
        generics.forEach { it.visit(this, compilationContext) }
    }

    override fun visitCovariantTypeNode(covariantTypeNode: CovariantTypeNode, compilationContext: CompilationContext) {
        covariantTypeNode.parentTypeNode.visit(this, compilationContext)
        compilationContext.currentFunction()
                .functionContext.genericTypesMap[covariantTypeNode.typeNode.symbol] = covariantTypeNode.parentTypeNode
    }

    override fun visitSingleTypeNode(t: SingleTypeNode, compilationContext: CompilationContext) {

    }

    override fun visit(fr: FunctionReturn, compilationContext: CompilationContext) {
        val typeNode = compilationContext.currentFunction()
                .functionContext.genericTypesMap[fr.typeNode.symbol] ?: throw CompileException("Unresolved type", fr.typeNode, compilationContext.currentModule)

        fr.typeNode.type = typeNode.type
        fr.typeNode.genericType = typeNode.genericType
    }

    override fun visit(fp: FunctionParameter, compilationContext: CompilationContext) {
        val typeNode = compilationContext.currentFunction()
                .functionContext.genericTypesMap[fp.typeNode.symbol] ?: throw CompileException("Unresolved type", fp.typeNode, compilationContext.currentModule)

        fp.typeNode.type = typeNode.type
        fp.typeNode.genericType = typeNode.genericType
    }
}
