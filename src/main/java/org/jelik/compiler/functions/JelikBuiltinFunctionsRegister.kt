package org.jelik.compiler.functions

import org.jelik.compiler.data.JelikBuiltinFunction
import org.jelik.compiler.data.MethodData
import org.jelik.compiler.lang.JelikAny
import org.jelik.types.JVMArrayType
import org.jelik.types.JVMIntType
import org.jelik.types.JVMObjectType
import org.jelik.types.JVMStringType
import org.jelik.types.JVMVoidType
import org.jelik.types.Type
import org.jelik.types.jvm.JVMByteType
import org.jelik.types.jvm.JVMDoubleType
import org.jelik.types.jvm.JVMFloatType
import org.jelik.types.jvm.JVMLongType
import org.jelik.types.jvm.JVMShortType
import org.objectweb.asm.Opcodes
import java.io.PrintStream

/**
 * @author Marcin Bukowiecki
 */
object JelikBuiltinFunctionsRegister {

    private val registers: List<MethodData> = listOf(
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "println",
                    listOf(JVMObjectType.INSTANCE),
                    listOf(JVMObjectType.INSTANCE),
                    JVMVoidType.INSTANCE,
                    JVMVoidType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                mv.getStatic(Type.of(System::class.java), "out", Type.of(PrintStream::class.java))
                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.invokeInstance(Type.of(PrintStream::class.java), "println", "(Ljava/lang/Object;)V")
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "println",
                    emptyList(),
                    emptyList(),
                    JVMVoidType.INSTANCE,
                    JVMVoidType.INSTANCE,
                    JelikAny.classData) { _, _, mv ->

                mv.getStatic(Type.of(System::class.java), "out", Type.of(PrintStream::class.java))
                mv.invokeInstance(Type.of(PrintStream::class.java), "println", "()V")
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "print",
                    listOf(JVMObjectType.INSTANCE),
                    listOf(JVMObjectType.INSTANCE),
                    JVMVoidType.INSTANCE,
                    JVMVoidType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                mv.getStatic(Type.of(System::class.java), "out", Type.of(PrintStream::class.java))
                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.invokeInstance(Type.of(PrintStream::class.java), "print", "(Ljava/lang/Object;)V")
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMByteType.INSTANCE)),
                    listOf(JVMArrayType(JVMByteType.INSTANCE)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMShortType.INSTANCE)),
                    listOf(JVMArrayType(JVMShortType.INSTANCE)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMIntType.INSTANCE)),
                    listOf(JVMArrayType(JVMIntType.INSTANCE)),
                            JVMIntType.INSTANCE,
                            JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMLongType.INSTANCE)),
                    listOf(JVMArrayType(JVMLongType.INSTANCE)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMFloatType.INSTANCE)),
                    listOf(JVMArrayType(JVMFloatType.INSTANCE)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMDoubleType.INSTANCE)),
                    listOf(JVMArrayType(JVMDoubleType.INSTANCE)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },
            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMStringType.INSTANCE)),
                    listOf(JVMArrayType(JVMStringType.INSTANCE)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },

            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(JVMArrayType(JVMObjectType.INSTANCE)),
                    listOf(JVMArrayType(JVMObjectType.INSTANCE)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.visitArrayLength()
            },

            JelikBuiltinFunction(Opcodes.ACC_STATIC or Opcodes.ACC_PUBLIC,
                    "len",
                    listOf(Type.of(Map::class.java)),
                    listOf(Type.of(Map::class.java)),
                    JVMIntType.INSTANCE,
                    JVMIntType.INSTANCE,
                    JelikAny.classData) { functionCallNode, toByteCodeVisitor, mv ->

                functionCallNode
                        .argumentList
                        .arguments
                        .forEach { arg -> arg.accept(toByteCodeVisitor, mv.compilationContext) }
                mv.invokeInstance(Type.of(Map::class.java).internalName,
                        "size",
                        "()I",
                        true)
            },
    )

    fun findByName(name: String): List<MethodData> {
        return registers.filter { m -> m.name == name }
    }
}
