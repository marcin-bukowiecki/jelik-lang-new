package org.jelik.parser.ast.resolvers;

import com.google.common.collect.Maps;
import org.jelik.types.JVMBooleanType;
import org.jelik.types.JVMIntType;
import org.jelik.types.JVMVoidType;
import org.jelik.types.Type;
import org.jelik.types.jvm.JVMByteType;
import org.jelik.types.jvm.JVMCharType;
import org.jelik.types.jvm.JVMDoubleType;
import org.jelik.types.jvm.JVMFloatType;
import org.jelik.types.jvm.JVMLongType;
import org.jelik.types.jvm.JVMShortType;

import java.util.Map;
import java.util.Optional;

/**
 * @author Marcin Bukowiecki
 */
public enum BuiltinTypeRegister {

    INSTANCE;

    private final Map<String, Type> mappedNames = Maps.newHashMap();

    BuiltinTypeRegister() {
        init(); //TODO move to other place
    }

    public void init() {
        mappedNames.put("Int", JVMIntType.INSTANCE);
        mappedNames.put("Long", JVMLongType.INSTANCE);
        mappedNames.put("Short", JVMShortType.INSTANCE);
        mappedNames.put("Byte", JVMByteType.INSTANCE);
        mappedNames.put("Float", JVMFloatType.INSTANCE);
        mappedNames.put("Double", JVMDoubleType.INSTANCE);
        mappedNames.put("Void", JVMVoidType.INSTANCE);
        mappedNames.put("Boolean", JVMBooleanType.INSTANCE);
        mappedNames.put("Char", JVMCharType.INSTANCE);
    }

    public Optional<Type> checkForBuiltinByName(final String name) {
        return Optional.ofNullable(mappedNames.get(name));
    }
}
