package org.jelik.parser.ast.resolvers.generics

import org.assertj.core.api.Assertions
import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.LiteralExpr
import org.jelik.parser.ast.arguments.ArgumentList
import org.jelik.parser.ast.functions.ConstructorTargetFunctionCallProvider
import org.jelik.parser.ast.functions.FunctionCallExpr
import org.jelik.parser.ast.types.SingleTypeNode
import org.jelik.parser.ast.types.TypeVariableListNode
import org.jelik.parser.ast.types.WildCardTypeNode
import org.jelik.parser.token.LiteralToken
import org.jelik.types.JVMIntType
import org.jelik.types.JVMObjectType
import org.jelik.types.Type
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ConstructorTypeParametersMapperTest {

    @Test
    fun testArrayList_1() {
        val ctx = CompilationContext()
        val expr = FunctionCallExpr(LiteralExpr(LiteralToken("ArrayList")), ArgumentList.EMPTY)
        expr.owner = Type.of(ArrayList::class.java)
        expr.literalExpr.typeParameterListNode = TypeVariableListNode(
            listOf(SingleTypeNode("Int")
                .let { t ->
                    t.type = JVMIntType.INSTANCE
                    t.genericType = JVMIntType.INSTANCE
                    t
                })
        )
        expr.targetFunctionCallProvider = ConstructorTargetFunctionCallProvider(Type.of(ArrayList::class.java)
            .findClassData(ctx)
            .constructorScope
            .first { c -> c.parameterTypes.size == 0 })

        ConstructorTypeParametersMapper.mapTypeParameters(expr)

        Assertions.assertThat(expr.genericReturnType.toString()).isEqualTo("java.util.ArrayList<Int>")
    }

    @Test
    fun testArrayList_2() {
        val ctx = CompilationContext()
        val expr = FunctionCallExpr(LiteralExpr(LiteralToken("ArrayList")), ArgumentList.EMPTY)
        expr.owner = Type.of(ArrayList::class.java)
        expr.literalExpr.typeParameterListNode = TypeVariableListNode(
            listOf(WildCardTypeNode()
                .let { t ->
                    t.type = JVMObjectType.INSTANCE
                    t.genericType = JVMObjectType.INSTANCE
                    t
                })
        )
        expr.targetFunctionCallProvider = ConstructorTargetFunctionCallProvider(Type.of(ArrayList::class.java)
            .findClassData(ctx)
            .constructorScope
            .first { c -> c.parameterTypes.size == 0 })

        ConstructorTypeParametersMapper.mapTypeParameters(expr)

        Assertions.assertThat(expr.genericReturnType.toString()).isEqualTo("java.util.ArrayList<java.lang.Object>")
    }
}
