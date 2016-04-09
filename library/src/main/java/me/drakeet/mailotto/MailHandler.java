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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MailHandler {

    /**
     * Object sporting the handler method.
     */
    private final Object target;
    /**
     * Handler method.
     */
    private final Method method;
    /**
     * Object hash code.
     */
    private final int hashCode;
    /**
     * Should this handler receive mail?
     */
    private boolean valid = true;


    MailHandler(Object target, Method method) {
        if (target == null) {
            throw new NullPointerException("MailHandler target cannot be null.");
        }
        if (method == null) {
            throw new NullPointerException("MailHandler method cannot be null.");
        }

        this.target = target;
        this.method = method;

        // 取消 Java语言访问检查，提高反射速度
        method.setAccessible(true);

        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
        final int prime = 31;
        hashCode = (prime + method.hashCode()) * prime + target.hashCode();
    }


    public boolean isValid() {
        return valid;
    }


    /**
     * If invalidated, will subsequently refuse to handle mails.
     * <p/>
     * Should be called when the wrapped object is unregistered from the MailBox.
     */
    public void invalidate() {
        valid = false;
    }


    /**
     * Invokes the wrapped handler method to handle {@code mail}.
     *
     * @param mail mail to handle
     * @throws IllegalStateException if previously invalidated.
     * @throws InvocationTargetException if the wrapped method throws any {@link Throwable} that is
     * not
     * an {@link Error} ({@code Error}s are propagated as-is).
     */
    public void handleMail(Object mail) throws InvocationTargetException {
        if (!valid) {
            throw new IllegalStateException(
                    toString() + " has been invalidated and can no longer handle mails.");
        }
        try {
            method.invoke(target, mail);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }


    @Override public String toString() {
        return "[MailHandler " + method + "]";
    }


    @Override public int hashCode() {
        return hashCode;
    }


    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final MailHandler other = (MailHandler) obj;
        return method.equals(other.method) && target == other.target;
    }
}
