/*
 * Copyright (C) 2016 drakeet.
 * Copyright (C) 2012 Square, Inc.
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

import android.os.Looper;

/**
 * Enforces a thread confinement policy for methods on a particular mailbox.
 *
 * @author Jake Wharton
 * @author drakeet
 */
public interface ThreadEnforcer {

    /**
     * Enforce a valid thread for the given {@code mailbox}. Implementations may throw any runtime
     * exception.
     *
     * @param mailbox Event bus instance on which an action is being performed.
     */
    void enforce(Mailbox mailbox);

    /**
     * A {@link ThreadEnforcer} that does no verification.
     */
    ThreadEnforcer ANY = new ThreadEnforcer() {
        @Override public void enforce(Mailbox mailbox) {
            // Allow any thread.
        }
    };

    /**
     * A {@link ThreadEnforcer} that confines {@link Mailbox} methods to the main thread.
     */
    ThreadEnforcer MAIN = new ThreadEnforcer() {
        @Override public void enforce(Mailbox mailbox) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException(
                        "Mailbox " + mailbox + " accessed from non-main thread " +
                                Looper.myLooper());
            }
        }
    };
}
