package com.hp.xo.resourcepool.utils;

import static java.beans.Introspector.getBeanInfo;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableSet;
import com.hp.xo.resourcepool.exception.CloudRuntimeException;

public class ReflectUtil {
    
    private static final Logger log = Logger.getLogger(ReflectUtil.class);

    public static Pair<Class<?>, Field> getAnyField(Class<?> clazz, String fieldName) {
        try {
            return new Pair<Class<?>, Field>(clazz, clazz.getDeclaredField(fieldName));
        } catch (SecurityException e) {
            throw new CloudRuntimeException("How the heck?", e);
        } catch (NoSuchFieldException e) {
            // Do I really want this?  No I don't but what can I do?  It only throws the NoSuchFieldException.
            Class<?> parent = clazz.getSuperclass();
            if (parent != null) {
                return getAnyField(parent, fieldName);
            }
            return null;
        }
    }
    
    public static Method findMethod(Class<?> clazz, String methodName) {
        do {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return null;
    }

    // Gets all classes with some annotation from a package
    public static Set<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> annotation,
                                                         String[] packageNames) {
        Reflections reflections;
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for(String packageName: packageNames) {
            reflections = new Reflections(packageName);
            classes.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return classes;
    }

    // Checks against posted search classes if cmd is async
    public static boolean isCmdClassAsync(Class<?> cmdClass,
                                          Class<?>[] searchClasses) {
        boolean isAsync = false;
        Class<?> superClass = cmdClass;

        while (superClass != null && superClass != Object.class) {
            String superName = superClass.getName();
            for (Class<?> baseClass: searchClasses) {
                if (superName.equals(baseClass.getName())) {
                    isAsync = true;
                    break;
                }
            }
            if (isAsync)
                break;
            superClass = superClass.getSuperclass();
        }
        return isAsync;
    }

    // Returns all fields until a base class for a cmd class
    public static List<Field> getAllFieldsForClass(Class<?> cmdClass,
                                                   Class<?> baseClass) {
        List<Field> fields = new ArrayList<Field>();
        Collections.addAll(fields, cmdClass.getDeclaredFields());
        Class<?> superClass = cmdClass.getSuperclass();
        while (baseClass.isAssignableFrom(superClass)) {
            Field[] superClassFields = superClass.getDeclaredFields();
            if (superClassFields != null)
                Collections.addAll(fields, superClassFields);
            superClass = superClass.getSuperclass();
        }
        return fields;
    }

    // Returns all unique fields except excludeClasses for a cmd class
    public static Set<Field> getAllFieldsForClass(Class<?> cmdClass,
                                                  Class<?>[] excludeClasses) {
        Set<Field> fields = new HashSet<Field>();
        Collections.addAll(fields, cmdClass.getDeclaredFields());
        Class<?> superClass = cmdClass.getSuperclass();

        while (superClass != null && superClass != Object.class) {
            String superName = superClass.getName();
            boolean isNameEqualToSuperName = false;
            for (Class<?> baseClass: excludeClasses)
                if (superName.equals(baseClass.getName()))
                    isNameEqualToSuperName = true;

            if (!isNameEqualToSuperName) {
                Field[] superClassFields = superClass.getDeclaredFields();
                if (superClassFields != null)
                    Collections.addAll(fields, superClassFields);
            }
            superClass = superClass.getSuperclass();
        }
        return fields;
    }

    public static List<String> flattenProperties(final Object target,
                                                 final Class<?> clazz) {
        return flattenPropeties(target, clazz, "class");
    }

    public static List<String> flattenPropeties(final Object target,
                                                final Class<?> clazz,
                                                final String...
                                                        excludedProperties) {
        return flattenProperties(target, clazz,
                ImmutableSet.copyOf(excludedProperties));
    }

    private static List<String> flattenProperties(final Object target,
                                                  final Class<?> clazz,
                                                  final ImmutableSet<String>
                                                          excludedProperties) {

        assert clazz != null;

        if (target == null) {
            return emptyList();
        }

        assert clazz.isAssignableFrom(target.getClass());

        try {

            final BeanInfo beanInfo = getBeanInfo(clazz);
            final PropertyDescriptor[] descriptors = beanInfo
                    .getPropertyDescriptors();

            final List<String> serializedProperties = new ArrayList<String>();
            for (final PropertyDescriptor descriptor : descriptors) {

                if (excludedProperties.contains(descriptor.getName())) {
                    continue;
                }

                serializedProperties.add(descriptor.getName());
                final Object value = descriptor.getReadMethod().invoke(target);
                serializedProperties.add(value != null ? value.toString()
                        : "null");

            }

            return unmodifiableList(serializedProperties);

        } catch (IntrospectionException e) {
            log.warn(
                    "Ignored IntrospectionException when serializing class "
                            + target.getClass().getCanonicalName(), e);
        } catch (IllegalArgumentException e) {
            log.warn(
                    "Ignored IllegalArgumentException when serializing class "
                            + target.getClass().getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            log.warn(
                    "Ignored IllegalAccessException when serializing class "
                            + target.getClass().getCanonicalName(), e);
        } catch (InvocationTargetException e) {
            log.warn(
                    "Ignored InvocationTargetException when serializing class "
                            + target.getClass().getCanonicalName(), e);
        }

        return emptyList();

    }


}
