package org.jelik.compiler.data;

import com.google.common.collect.ImmutableList;
import org.jelik.compiler.CompilationContext;
import org.jelik.compiler.asm.utils.ASMUtils;
import org.jelik.types.FunctionType;
import org.jelik.types.Type;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcin Bukowiecki
 */
public class ClassDataImpl implements ClassData {

    protected final String canonicalName;
    protected final String name;
    protected final int modifiers;
    protected final boolean functionalInterface;
    protected final Type type;
    protected final boolean interfacee;
    protected final boolean abstractt;

    //initialize after constructor call
    protected List<MethodDataImpl> methodScope = Collections.emptyList();
    protected List<MethodDataImpl> constructorScope = Collections.emptyList();
    protected List<FieldDataImpl> fieldScope = Collections.emptyList();
    protected List<ClassData> interfaceScope = Collections.emptyList();
    protected ClassData parentScope;

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
        this.abstractt = Modifier.isAbstract(this.modifiers);
    }

    @Override
    public @NotNull List<? extends MethodData> findByName(String name, CompilationContext compilationContext) {
        return new ImmutableList.Builder<MethodData>()
                .addAll(methodScope.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList()))
                .addAll(constructorScope.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList()))
                .addAll("java.lang.Object".equals(canonicalName) || parentScope == null ?
                        Collections.emptySet() :
                        parentScope.findByName(name, compilationContext))
                .addAll(interfaceScope.stream()
                        .map(i -> i.findByName(name, compilationContext)).flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<FieldData> findFieldByName(String name) {
        return fieldScope.stream()
                .filter(m -> m.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasSuperClass() {
        return parentScope != null;
    }

    @Override
    public List<Type> getInterfaceTypes() {
        return interfaceScope.stream()
                .map(ClassData::getType)
                .collect(Collectors.toList());
    }

    @Override
    public List<MethodData> getMethodScope() {
        return Collections.unmodifiableList(methodScope);
    }

    @Override
    public Type getParentType() {
        return parentScope.getType();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Collection<? extends MethodData> getConstructorScope() {
        return constructorScope;
    }

    @Override
    public boolean isInterface() {
        return interfacee;
    }

    @Override
    public boolean isAbstract() {
        return abstractt;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public String getName() {
        return name;
    }

    public int getModifiers() {
        return modifiers;
    }

    public boolean isFunctionalInterface() {
        return functionalInterface;
    }

    public boolean isInterfacee() {
        return interfacee;
    }

    public boolean isAbstractt() {
        return abstractt;
    }

    public List<FieldDataImpl> getFieldScope() {
        return fieldScope;
    }

    public List<ClassData> getInterfaceScope() {
        return interfaceScope;
    }

    public ClassData getParentScope() {
        return parentScope;
    }
}
