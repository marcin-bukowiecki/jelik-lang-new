package org.jelik.parser.ast.functions.providers

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.data.JelikBuiltinFunction
import org.jelik.parser.ast.functions.FunctionCallExpr

/**
 * @author Marcin Bukowiecki
 */
class BuiltinFunctionCallProvider(methodData: JelikBuiltinFunction) : StaticTargetFunctionCallProvider(methodData) {

    override fun getCodeGenProvider(): (FunctionCallExpr, ToByteCodeVisitor, MethodVisitorAdapter) -> Unit {
        return (methodData as JelikBuiltinFunction).byteCodeData
    }
}
