package org.jelik.compiler.data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Metadata for Java class
 *
 * @author Marcin Bukowiecki
 */
public class JavaClassData extends ClassDataImpl {

    public JavaClassData(Class<?> clazz) {
        super(clazz);
    }

    public void resolveMethodScope(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        if (declaredMethods.length == 0) {
            this.methodScope = Collections.emptyList();
        } else if (isArray()) {
            //this.methodScope = new JdkArrayMethodScope(jelikCompiler, this);
        } else {
            this.methodScope = Arrays.stream(declaredMethods)
                    .map(m -> new MethodDataImpl(m, this))
                    .collect(Collectors.toList());
            this.constructorScope = Arrays.stream(clazz.getDeclaredConstructors())
                    .map(m -> new MethodDataImpl(m, this))
                    .collect(Collectors.toList());
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        this.fieldScope = Arrays.stream(declaredFields).map(f -> new FieldDataImpl(f, this)).collect(Collectors.toList());;
    }

    public void resolveParentScope(Class<?> clazz) {/*
        final Class<?> parentClass = clazz.getSuperclass();
        final java.lang.reflect.Type genericSuperclass = clazz.getGenericSuperclass();

        if (parentClass == null) {
            if (isInterface()) {
                this.parentScope = new JdkParentScope(jelikCompiler.loadClass(Object.class));
            } else {
                if (clazz.equals(Object.class)) {
                    this.parentScope = new JdkParentScope(new ParentClassEnd());
                } else {
                    this.parentScope = new JdkParentScope(jelikCompiler.loadClass(Object.class));
                }
            }
        } else {
            this.parentScope = new JdkParentScope(jelikCompiler.loadClass(parentClass));
        }*/
    }

    public void resolveInterfaceScope(Class<?> clazz) {
        var interfaces = clazz.getInterfaces();
        var genericInterfaces = clazz.getGenericInterfaces();
        if (interfaces.length == 0) {
            this.interfaceScope = Collections.emptyList();
        } else {
            this.interfaceScope = Arrays.stream(interfaces).map(JavaClassData::new).collect(Collectors.toList());
        }
    }

    public boolean isInterface() {
        return type.isInterface();
    }

    public boolean isArray() {
        return false;
    }
}
