/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package c10n.tools.inspector;

import com.google.common.collect.Sets;

import java.lang.annotation.Annotation;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author rodion
 */
public final class C10NTranslations {
    private final Set<ResourceBundle> bundles = Sets.newHashSet();
    private final Set<Class<? extends Annotation>> annotations = Sets.newHashSet();

    public Set<ResourceBundle> getBundles() {
        return bundles;
    }

    public Set<Class<? extends Annotation>> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "C10NTranslations{" +
                "bundles=" + bundles +
                ", annotations=" + annotations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        C10NTranslations that = (C10NTranslations) o;

        if (!annotations.equals(that.annotations)) return false;
        if (!bundles.equals(that.bundles)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bundles.hashCode();
        result = 31 * result + annotations.hashCode();
        return result;
    }
}