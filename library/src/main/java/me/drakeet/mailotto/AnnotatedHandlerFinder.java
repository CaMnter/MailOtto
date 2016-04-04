/*
 * Copyright (C) 2016 drakeet.
 * Copyright (C) 2012 Square, Inc.
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.drakeet.mailotto;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Helper methods for finding methods annotated with {@link OnMailReceived}(subscriber).
 *
 * @author Cliff Biffle
 * @author Louis Wasserman
 * @author Jake Wharton
 * @author drakeet
 */
final class AnnotatedHandlerFinder {

    /**
     * Cache event bus subscriber methods for each class.
     */
    private static final ConcurrentMap<Class<?>, Method> SUBSCRIBER_CACHE
            = new ConcurrentHashMap<>();


    private AnnotatedHandlerFinder() {
        // No instances.
    }


    /**
     * get a method annotated with {@link OnMailReceived} into their respective
     * caches for the specified class.
     */
    private static Method loadAnnotatedMethod(Class<?> listenerClass) {
        for (Method method : listenerClass.getDeclaredMethods()) {
            // The compiler sometimes creates synthetic bridge methods as part of the
            // type erasure process. As of JDK8 these methods now include the same
            // annotations as the original declarations. They should be ignored for
            // OnMailReceived
            if (method.isBridge()) {
                continue;
            }
            if (method.isAnnotationPresent(OnMailReceived.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException(
                            "Method " + method + " has @OnMailReceived annotation but requires " +
                                    parameterTypes.length +
                                    " arguments.  Methods must require a single argument.");
                }

                Class<?> mailType = parameterTypes[0];
                if (mailType.isInterface()) {
                    throw new IllegalArgumentException(
                            "Method " + method + " has @OnMailReceived annotation on " + mailType +
                                    " which is an interface.  Subscription must be on a concrete class type.");
                }

                if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                    throw new IllegalArgumentException(
                            "Method " + method + " has @OnMailReceived annotation on " + mailType +
                                    " but is not 'public'.");
                }
                return method;
            }
        }
        return null;
    }


    /**
     * This implementation finds all methods marked with a {@link OnMailReceived} annotation.
     */
    static MailHandler findOnMailReceived(Object listener) {
        Class<?> listenerClass = listener.getClass();
        // todo: 一个 Mail 只能有一个方法！
        Method method = SUBSCRIBER_CACHE.get(listenerClass);

        if (method == null) {
            method = loadAnnotatedMethod(listenerClass);
            if (method == null) {
                throw new IllegalStateException(
                        "You must set a @OnMailReceived method for handle mail.");
            } else {
                SUBSCRIBER_CACHE.put(listenerClass, method);
            }
        }
        // method != null
        return new MailHandler(listener, method);
    }
}
