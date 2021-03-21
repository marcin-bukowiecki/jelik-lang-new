package org.jelik.compiler.data;

import lombok.Getter;
import org.jelik.CompilationContext;
import org.jelik.compiler.asm.utils.ASMUtils;
import org.jelik.types.FunctionType;
import org.jelik.types.Type;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
@Getter
public class ClassDataImpl implements ClassData {

    protected final String canonicalName;
    protected final String name;
    protected final int modifiers;
    protected final boolean functionalInterface;
    protected final Type type;
    protected final boolean interfacee;

    //initialize after constructor call
    protected List<MethodDataImpl> methodScope;
    protected List<MethodDataImpl> constructorScope;
    protected List<FieldDataImpl> fieldScope;
    protected List<ClassDataImpl> interfaceScope = Collections.emptyList();
    protected ClassDataImpl parentScope;

    public ClassDataImpl(Class<?> clazz) {
        this.canonicalName = clazz.getCanonicalName() == null ? "" : clazz.getCanonicalName();
        this.name = clazz.getName();
        this.modifiers = clazz.getModifiers();
        this.functionalInterface = ASMUtils.isFunctionalInterface(clazz);
        this.interfacee = clazz.isInterface();
        if (this.functionalInterface) {
            this.type = new FunctionType(clazz);
        } else {
            this.type = Type.of(clazz);
        }
    }

    @Override
    public List<MethodData> findByName(String name, CompilationContext compilationContext) {
        return methodScope.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public List<FieldData> findFieldByName(String name) {
        return fieldScope.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public boolean hasSuperClass() {
        return parentScope != null;
    }

    @Override
    public List<Type> getInterfaceTypes() {
        return interfaceScope.stream().map(cd -> cd.type).collect(Collectors.toList());
    }

    public List<MethodDataImpl> getMethodScope() {
        return Collections.unmodifiableList(methodScope);
    }

    @Override
    public Type getParentType() {
        return parentScope.type;
    }
}
