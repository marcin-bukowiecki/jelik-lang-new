package org.jelik.types;

import org.jelik.compiler.common.TypeEnum;

import java.util.Objects;

public class JelikTypeVariable extends Type {

    public JelikTypeVariable(String name) {
        super(name, name, TypeEnum.typeVariable);
    }

    public boolean matches(Type type) {
        if (type instanceof JelikTypeVariable) {
            return type.getName().equals(this.name);
        } else {
            return this.equals(type);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj instanceof JelikTypeVariable) {
            return this.name.equals(((JelikTypeVariable) obj).name);
        }

        return false;
    }
}
