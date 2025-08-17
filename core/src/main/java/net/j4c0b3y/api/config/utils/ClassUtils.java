package net.j4c0b3y.api.config.utils;

import lombok.experimental.UtilityClass;

import java.lang.invoke.MethodType;
import java.lang.reflect.*;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@UtilityClass
public class ClassUtils {
    /**
     * Gets the generic types for a field.
     *
     * @param field The field.
     * @return The generic types.
     */
    public Type[] getGenerics(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return type.getActualTypeArguments();
    }

    /**
     * Gets the wrapped type of the primitive type.
     *
     * @param type The primitive type.
     * @return The wrapper class.
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> wrap(Class<T> type) {
        return (Class<T>) MethodType.methodType(type).wrap().returnType();
    }

    /**
     * Gets the name of a member.
     *
     * @param member The member.
     * @return The name.
     */
    public String getName(AnnotatedElement member) {
        if (member instanceof Class) {
            return ((Class<?>) member).getSimpleName();
        }

        if (member instanceof Field) {
            return ((Field) member).getName();
        }

        throw new IllegalArgumentException("Argument 'member' must be a class or field.");
    }

    public boolean isCompanionClass(Class<?> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) return false;
        if (!Modifier.isStatic(clazz.getModifiers())) return false;

        return clazz.getSimpleName().equals("Companion");
    }

    public boolean isCompanionField(Field field) {
        if (!isCompanionClass(field.getType())) return false;
        if (!Modifier.isStatic(field.getModifiers())) return false;
        if (!Modifier.isFinal(field.getModifiers())) return false;

        return field.getName().equals("Companion");
    }
}
