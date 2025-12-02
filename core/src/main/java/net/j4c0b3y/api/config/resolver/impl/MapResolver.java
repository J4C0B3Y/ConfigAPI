package net.j4c0b3y.api.config.resolver.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.impl.MapProvider;
import net.j4c0b3y.api.config.resolver.TypeResolver;
import net.j4c0b3y.api.config.utils.ClassUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Resolves string to ? maps to our inbuilt map provider.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 17/11/2024
 */
@RequiredArgsConstructor
public class MapResolver implements TypeResolver {
    /**
     * The config handler.
     */
    private final ConfigHandler handler;

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeProvider<T> resolve(Class<T> type, Field field) {
        // If the value is not a map, return.
        if (!Map.class.isAssignableFrom(type)) return null;

        // If the map key generic is not a string, return.
        Type[] generics = ClassUtils.getGenerics(field);
        if (!generics[0].equals(String.class)) return null;

        if (generics[1] instanceof ParameterizedTypeImpl) {
            throw new IllegalArgumentException("Invalid type for '" + field.getName() + "', nested map?");
        }

        // Create a new map provider for the specified type.
        return (TypeProvider<T>) resolve((Class<?>) generics[1]);
    }

    private <E, T extends Map<String, E>> MapProvider<E, T> resolve(Class<E> type) {
        return new MapProvider<>(type, handler);
    }
}
