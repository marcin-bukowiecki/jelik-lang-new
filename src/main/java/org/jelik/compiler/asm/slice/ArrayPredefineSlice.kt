package org.jelik.compiler.asm.slice

import org.jelik.compiler.CompilationContext
import org.jelik.compiler.asm.MethodVisitorAdapter
import org.jelik.compiler.asm.utils.ASMUtils
import org.jelik.compiler.asm.visitor.ToByteCodeResult
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor
import org.jelik.compiler.runtime.TypeEnum
import org.jelik.types.Type
import org.objectweb.asm.Opcodes

/**
 * @author Marcin Bukowiecki
 */
object ArrayPredefineSlice {

    private val predefinedSliceFunctions = mutableMapOf<Type, Type>()

    var counter = 1

    fun predefineForType(type: Type, byteCodeVisitor: ToByteCodeVisitor, ctx: CompilationContext): Type {
        if (type in predefinedSliceFunctions) {
            return predefinedSliceFunctions[type]!!
        }

        val name = "jelik.lang.JelikUtilsSlice$counter"
        val cw = ASMUtils.createClassWithDefaultConstructor(name)
        val mv = MethodVisitorAdapter(cw.visitMethod(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                "arraySlice",
                "(" + type.descriptor + "II)" + type.descriptor + "", null, null),
                ctx)
        mv.aload(0)
        mv.intLoad(1)
        mv.invokeStatic("jelik/lang/JelikUtils",
                "calculateArrayIndex",
                "([Ljava/lang/Object;I)I")
        mv.intStore(3)
        mv.aload(0)
        mv.intLoad(2)
        mv.invokeStatic("jelik/lang/JelikUtils",
                "calculateArrayIndex",
                "([Ljava/lang/Object;I)I")
        mv.intStore(4)
        mv.intLoad(4)
        mv.intLoad(3)
        mv.visitIntSub()
        mv.newArray(type.getInnerType(0))
        mv.objectStore(5)
        mv.aload(0)
        mv.intLoad(3)
        mv.aload(5)
        mv.pushInt(0)
        mv.intLoad(4)
        mv.intLoad(3)
        mv.visitIntSub()
        mv.invokeStatic("java/lang/System",
                "arraycopy",
                "(Ljava/lang/Object;ILjava/lang/Object;II)V")
        mv.aload(5)
        mv.visitAReturn()
        mv.visitMaxes(6,6)

        val t = Type("JelikUtilsSlice$counter",
                "jelik.lang.JelikUtilsSlice$counter",
                TypeEnum.objectT)
        byteCodeVisitor.addByteCodeResult(ToByteCodeResult(t, cw.bytes, false))
        predefinedSliceFunctions[type] = t
        counter++
        return t
    }
}
