package com.arsuhinars.animals_chipization.core.util;

public class ErrorDetailsFormatter {
    private static final String NOT_FOUND_FORMAT = "%s with %s=%s was not found";
    private static final String DEPENDS_ON_FORMAT = "%s depends on %s";
    private static final String ALREADY_EXISTS_FORMAT = "%s with %s=%s already exists";
    private static final String ALREADY_CONTAINS_FORMAT = "%s already contains %s in %s";
    private static final String DOES_NOT_CONTAIN_FORMAT = "%s doesn't contain %s in %s";
    private static final String LAST_ITEM_FORMAT = "%s is last item in %s of %s";

    public static String formatNotFoundError(Class<?> entityClass, Long entityId) {
        return formatNotFoundError(entityClass, "id", entityId);
    }

    public static String formatNotFoundError(Class<?> entityClass, String valueName, Object value) {
        return String.format(NOT_FOUND_FORMAT, entityClass.getSimpleName(), valueName, value);
    }

    public static String formatDependsOnError(Object entity, Class<?> dependOnEntityClass) {
        return String.format(DEPENDS_ON_FORMAT, entity.toString(), dependOnEntityClass.getSimpleName());
    }

    public static String formatAlreadyExistsError(Class<?> entityClass, String valueName, Object value) {
        return String.format(ALREADY_EXISTS_FORMAT, entityClass.getSimpleName(), valueName, value);
    }

    public static String formatAlreadyContainsError(Object entity, Object value, String propertyName) {
        return String.format(ALREADY_CONTAINS_FORMAT, entity, value, propertyName);
    }

    public static String formatDoesNotContainError(Object entity, Object value, String propertyName) {
        return String.format(DOES_NOT_CONTAIN_FORMAT, entity, value, propertyName);
    }

    public static String formatLastItemError(Object entity, Object value, String propertyName) {
        return String.format(LAST_ITEM_FORMAT, value, propertyName, entity);
    }
}
