//
// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
//

package com.huawei.vmware.util;

import java.io.Serializable;

/**
 * Pair<>
 *
 * @param <T> T
 * @param <U> U
 * @author Administrator
 * @since 2020-12-09
 */
public class Pair<T, U> implements Serializable {
    private static final long serialVersionUID = 2L;
    T t;
    U u;

    /**
     * Pair
     */
    protected Pair() {
    }

    /**
     * Pair
     *
     * @param t t
     * @param u u
     */
    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    /**
     * first
     *
     * @return T
     */
    public T first() {
        return t;
    }

    /**
     * first
     *
     * @param value value
     * @return T
     */
    public T first(T value) {
        t = value;
        return t;
    }

    /**
     * second
     *
     * @return U
     */
    public U second() {
        return u;
    }

    /**
     * second
     *
     * @param value value
     * @return U
     */
    public U second(U value) {
        u = value;
        return u;
    }

    /**
     * set
     *
     * @param t t
     * @param u u
     */
    public void set(T t, U u) {
        this.t = t;
        this.u = u;
    }

    @Override
    // Note: This means any two pairs with null for both values will match each
    // other but what can I do?  This is due to stupid type erasure.
    public int hashCode() {
        return (t != null ? t.hashCode() : 0) | (u != null ? u.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair<?, ?> that = (Pair<?, ?>) obj;
        return (t != null ? t.equals(that.t) : that.t == null) && (u != null ? u.equals(that.u) : that.u == null);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("P[");
        b.append((t != null) ? t.toString() : "null");
        b.append(":");
        b.append((u != null) ? u.toString() : "null");
        b.append("]");
        return b.toString();
    }
}
