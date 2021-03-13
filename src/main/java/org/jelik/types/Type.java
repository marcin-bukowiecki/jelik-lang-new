package org.jelik.types;

import org.jelik.CompilationContext;
import org.jelik.compiler.JelikCompiler;
import org.jelik.compiler.asm.visitor.TypeVisitor;
import org.jelik.compiler.common.TypeEnum;
import org.jelik.compiler.data.ClassDataImpl;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.casts.CastObjectToObjectNode;
import org.jelik.parser.ast.numbers.Int32ToWrapperNode;
import org.jelik.parser.ast.resolvers.DefaultImportedTypeResolver;
import org.jelik.parser.ast.types.JelikWildCardType;
import org.jelik.parser.ast.types.TypeMappingContext;
import org.jelik.types.jvm.IntegerWrapperType;
import org.jelik.types.resolver.TypeVariablesHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base class for all types
 *
 * @author Marcin Bukowiecki
 */
public class Type {

    protected final String name;

    protected final String canonicalName;

    public final TypeEnum typeEnum;

    //i.e. List<E>
    private final List<Type> typeParameters;

    //i.e. List<String>
    private final List<Type> typeVariables;

    public Type(String name, String canonicalName, TypeEnum typeEnum) {
        this.name = name;
        this.canonicalName = canonicalName;
        this.typeEnum = typeEnum;
        this.typeVariables = Collections.emptyList();
        this.typeParameters = Collections.emptyList();
    }

    protected Type(Class<?> clazz) {
        this.name = clazz.getSimpleName();
        if (clazz.getCanonicalName() == null) {
            this.canonicalName = this.name;
        } else {
            this.canonicalName = clazz.getCanonicalName();
        }
        this.typeEnum = TypeEnum.objectT;
        this.typeParameters = TypeVariablesHelper.resolveTypeVariables(clazz);
        this.typeVariables = TypeVariablesHelper.resolveTypeVariables(clazz);
    }

    public static Type of(Class<?> clazz) {
        if (clazz.isInterface()) {
            return new InterfaceType(clazz);
        }
        if (clazz.isPrimitive()) {
            return PrimitiveTypeFactory.of(clazz);
        }
        if (clazz.isArray()) {
            return new JVMArrayType(Type.of(clazz.getComponentType()));
        }
        if (clazz.getCanonicalName() != null) {
            Type type = DefaultImportedTypeResolver.getType(clazz.getCanonicalName());
            if (type != null) {
                return type;
            }
        }
        return new Type(clazz);
    }

    public Type(String name,
                String canonicalName,
                TypeEnum typeEnum,
                List<Type> typeParameters,
                List<Type> typeVariables) {

        this.name = name;
        this.canonicalName = canonicalName;
        this.typeEnum = typeEnum;
        this.typeVariables = typeVariables;
        this.typeParameters = typeParameters;
    }

    public String getName() {
        return name;
    }

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public List<Type> getTypeVariables() {
        return typeVariables;
    }

    public List<Type> getTypeParameters() {
        return typeParameters;
    }

    public String forErrorMessage() {
        return "'" + canonicalName + "'";
    }

    public Type deepGenericCopy() {
        return new Type(
                name,
                canonicalName,
                typeEnum,
                typeVariables
                        .stream()
                        .map(Type::deepGenericCopy)
                        .collect(Collectors.toList()),
                typeParameters
                        .stream()
                        .map(Type::deepGenericCopy)
                        .collect(Collectors.toList())
        );
    }

    public ClassDataImpl findClassData(CompilationContext compilationContext) {
        ClassDataImpl classData = JelikCompiler.INSTANCE.classDataRegister.get(this.canonicalName);
        if (classData != null) {
            return classData;
        } else {
            JelikCompiler.INSTANCE.findClass(this.canonicalName);
            return JelikCompiler.INSTANCE.classDataRegister.get(this.canonicalName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;
        Type type = (Type) o;
        return canonicalName.equals(type.getCanonicalName());
    }

    public boolean isArray() {
        return false;
    }

    public boolean isCollection(CompilationContext compilationContext) {
        return this.canonicalName.equals(Collection.class.getCanonicalName()) ||
                compilationContext.findClassData(Collection.class.getCanonicalName())
                        .map(cd -> this.isAssignableTo(cd.getType(), compilationContext))
                        .orElse(false);
    }

    @Override
    public int hashCode() {
        return canonicalName.hashCode();
    }

    @Override
    public String toString() {
        if (typeVariables.isEmpty()){
            return canonicalName;
        }
        return canonicalName + "<" + typeVariables.stream()
                .map(Type::toString)
                .collect(Collectors.joining(",")) + ">";
    }

    public boolean isAssignableTo(Type type, CompilationContext compilationContext) {
        if (this.equals(type)) {
            return true;
        }

        if (type instanceof JVMObjectType) {
            return true;
        }

        //if (type instanceof NullableType) {
            //return isAssignableTo(type.getInnerType());
        //}
        Set<Type> assignableToTypes = getAssignableToTypes(compilationContext);
        for (Type t: assignableToTypes) {
            if (t == null) {
                continue;
            }

            if (t.equals(type)) {
                return true;
            } else {
                boolean result = t.isAssignableTo(type, compilationContext);

                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInterface() {
        return false;
    }

    public Set<Type> getAssignableToTypes(CompilationContext compilationContext) {
        return JelikCompiler.INSTANCE.findClassData(this.canonicalName, compilationContext)
                .map(classData -> {
                    Set<Type> result = new HashSet<>();
                    if (classData.hasSuperClass()) {
                        result.add(classData.getParentType());
                    }
                    result.addAll(classData.getInterfaceTypes());
                    return result;
                }).orElse(Collections.emptySet());
    }

    /**
     * i.e. if Stream<Int> is defined this method will return a Map { T -> Int }
     *
     * @return type mappings
     */
    public TypeMappingContext getMappings() {
        Map<Type, Type> result = new HashMap<>();

        int i = 0;
        for (Type typeParameter : typeParameters) {
            result.put(typeParameter, typeVariables.get(i));
            i++;
        }

        return new TypeMappingContext(result);
    }

    public String getDescriptor() {
        return "L" + canonicalName.replace('.','/') + ";";
    }

    public void setTypeVariable(Type key, Type value) {
        int i = 0;
        for (Type typeVariable : getTypeVariables()) {
            //TODO change to matches
            if (typeVariable.equals(key)) {
                typeVariables.set(i, value);
            } else if (typeVariable instanceof JelikWildCardType) {
                typeVariables.set(i, value);
            } else {
                typeVariable.setTypeVariable(key, value);
            }
            i++;
        }
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isLong() {
        return false;
    }

    public void visit(TypeVisitor typeVisitor, CompilationContext compilationContext) {
        typeVisitor.visit(this, compilationContext);
    }

    public String getInternalName() {
        return this.canonicalName.replace('.','/');
    }

    public void castFrom(Expression expression, JVMIntType type, CompilationContext compilationContext) {
        expression.parent.replaceWith(expression, new Int32ToWrapperNode(expression));
    }

    public void castFrom(Expression expression, Type type, CompilationContext compilationContext) {
        if (type.equals(this)) {
            return;
        }
        expression.parent.replaceWith(expression, new CastObjectToObjectNode(expression, expression.getFurtherExpression(), type, this));
    }

    public void castFrom(Expression expression, JVMObjectType type, CompilationContext compilationContext) {
        if (type.equals(this)) {
            return;
        }
        expression.parent.replaceWith(expression, new CastObjectToObjectNode(expression, expression.getFurtherExpression(), type, this));
    }


    public void castFrom(Expression expression, IntegerWrapperType type, CompilationContext compilationContext) {

    }

    public int getAssignableTypeAndHierarchyDepth(Type target, CompilationContext compilationContext) {
        if (this.equals(target)) {
            return 0;
        }
        return getAssignableTypeAndHierarchyDepth(1, target, compilationContext);
    }

    private int getAssignableTypeAndHierarchyDepth(int depth, Type target, CompilationContext compilationContext) {
        for (Type type : getAssignableToTypes(compilationContext)) {
            if (type.equals(target)) {
                return depth;
            } else {
                int result = type.getAssignableTypeAndHierarchyDepth(depth+1, target, compilationContext);

                if (result < 100) {
                    return result;
                }
            }
        }

        return 100;
    }

    public Type getPrimitiveType() {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    public boolean isWrapper() {
        return false;
    }

    public boolean isVoid() {
        return false;
    }

    public boolean isPrimitive() {
        return false;
    }

    public Type getInnerType(int i) {
        throw new UnsupportedOperationException(this.getClass().getCanonicalName());
    }

    public boolean isFunctionType() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }
}
