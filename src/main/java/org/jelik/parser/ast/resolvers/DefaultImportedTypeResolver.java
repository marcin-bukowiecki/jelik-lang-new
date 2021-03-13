package org.jelik.parser.ast.resolvers;

import com.google.common.collect.Maps;
import org.jelik.CompilationContext;
import org.jelik.parser.ast.visitors.AstVisitor;
import org.jelik.parser.ast.types.SingleTypeNode;
import org.jelik.types.JVMObjectType;
import org.jelik.types.JVMStringType;
import org.jelik.types.Type;
import org.jelik.types.jvm.ByteWrapperType;
import org.jelik.types.jvm.CharWrapperType;
import org.jelik.types.jvm.DoubleWrapperType;
import org.jelik.types.jvm.FloatWrapperType;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jelik.types.jvm.LongWrapperType;
import org.jelik.types.jvm.ShortWrapperType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public class DefaultImportedTypeResolver extends AstVisitor {

    private static final Map<String, Type> defaultClasses = Maps.newHashMap();

    static {
        defaultClasses.put("String", JVMStringType.INSTANCE);
        defaultClasses.put("Object", JVMObjectType.INSTANCE);
        defaultClasses.put("System", Type.of(System.class));
        defaultClasses.put("StringBuilder", Type.of(StringBuilder.class));

        defaultClasses.put("Integer", IntegerWrapperType.INSTANCE);
        defaultClasses.put("Long", LongWrapperType.INSTANCE);
        defaultClasses.put("Byte", ByteWrapperType.INSTANCE);
        defaultClasses.put("Double", DoubleWrapperType.INSTANCE);
        defaultClasses.put("Float", FloatWrapperType.INSTANCE);
        defaultClasses.put("Character", CharWrapperType.INSTANCE);
        defaultClasses.put("Short", ShortWrapperType.INSTANCE);

        defaultClasses.put("Exception", Type.of(Exception.class));
        defaultClasses.put("Throwable", Type.of(Throwable.class));

        defaultClasses.put("java.lang.String", JVMStringType.INSTANCE);
        defaultClasses.put("java.lang.Object", JVMObjectType.INSTANCE);
        defaultClasses.put("java.lang.StringBuilder", Type.of(StringBuilder.class));
        defaultClasses.put("java.lang.Integer", IntegerWrapperType.INSTANCE);
        defaultClasses.put("java.lang.Long", LongWrapperType.INSTANCE);
        defaultClasses.put("java.lang.Byte", ByteWrapperType.INSTANCE);
        defaultClasses.put("java.lang.Double", DoubleWrapperType.INSTANCE);
        defaultClasses.put("java.lang.Float", FloatWrapperType.INSTANCE);
        defaultClasses.put("java.lang.Char", CharWrapperType.INSTANCE);
        defaultClasses.put("java.lang.Short", ShortWrapperType.INSTANCE);
        defaultClasses.put("java.lang.Exception", Type.of(Exception.class));
        defaultClasses.put("java.lang.Throwable", Type.of(Throwable.class));
    }

    private Type type;

    @Override
    public void visit(@NotNull SingleTypeNode typeNode, @NotNull CompilationContext compilationContext) {
        String text = typeNode.getText();
        if (defaultClasses.containsKey(text)) {
            this.type = defaultClasses.get(text);
            typeNode.setType(this.type);
            typeNode.setGenericType(this.type.deepGenericCopy());
        }
    }

    public Optional<Type> getTypeOpt() {
        return Optional.ofNullable(type);
    }

    public static Optional<Type> getTypeOpt(String name) {
        return Optional.ofNullable(defaultClasses.get(name));
    }

    public static Type getType(String name) {
        return defaultClasses.get(name);
    }
}
