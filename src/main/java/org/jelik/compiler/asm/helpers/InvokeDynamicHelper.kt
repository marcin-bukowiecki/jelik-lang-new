package org.jelik.compiler.asm.helpers

import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.data.MethodData
import org.jelik.types.JVMObjectType
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * @author Marcin Bukowiecki
 */
object InvokeDynamicHelper {

    fun codeGen(mv: MethodVisitorAdapter, targetFunction: MethodData, functionalMethodName: String) {
        val targetFunctionName = targetFunction.name
        val targetFunctionDescriptor = targetFunction.descriptor
        val owner = targetFunction.owner.internalName

        val lambdaMetaFactoryHandler = Handle(Opcodes.H_INVOKESTATIC,
                "java/lang/invoke/LambdaMetafactory",
                "metafactory",
                "(Ljava/lang/invoke/MethodHandles\$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;" +
                        "Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)" +
                        "Ljava/lang/invoke/CallSite;", false)
        val targetFunctionHandler = Handle(Opcodes.H_INVOKESTATIC,
                owner,
                targetFunctionName,
                targetFunctionDescriptor,
                false)

        //parameters
        val interfaceArgs = arrayOfNulls<Type>(targetFunction.parameterTypes.size)
        for (i in targetFunction.parameterTypes.indices) {
            interfaceArgs[i] = Type.getType(Any::class.java)
        }

        //handle kind
        val targetFunctionArgs = arrayOfNulls<Type>(targetFunction.parameterTypes.size)
        for ((i, type) in targetFunction.parameterTypes.withIndex()) {
            targetFunctionArgs[i] = Type.getType(type.wrapperType.descriptor)
        }

        val resultType: Type = Type.getType(targetFunction.returnType.wrapperType.descriptor)

        val methodSignature = "()" + targetFunction.functionType.descriptor

        mv.visitInvokeDynamicInsn(
                functionalMethodName,
                methodSignature,  //method signature
                lambdaMetaFactoryHandler,
                Type.getMethodType(Type.getType(JVMObjectType.INSTANCE.descriptor), *interfaceArgs),
                targetFunctionHandler,
                Type.getMethodType(resultType, *targetFunctionArgs),  //(II)I won't work must be objects
                targetFunction.parameterTypes.size,
                targetFunction.returnType
        )
    }
}
