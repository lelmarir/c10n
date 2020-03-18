/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package com.github.rodionmoiseev.c10n.guice;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.github.rodionmoiseev.c10n.C10N;
import com.github.rodionmoiseev.c10n.C10NConfigBase;
import com.github.rodionmoiseev.c10n.C10NEnumMessages;
import com.github.rodionmoiseev.c10n.C10NFilters;
import com.github.rodionmoiseev.c10n.C10NMessages;
import com.google.inject.AbstractModule;

@SuppressWarnings("WeakerAccess") // rationale: public API
public class C10NModule extends AbstractModule {
    private final String[] packagePrefixes;

    private boolean configured = false;
    private final List<C10NConfigBase> configs = new LinkedList<>();

    public static C10NModule scanAllPackages() {
        return scanPackages("");
    }

    public static C10NModule scanPackages(String... packagePrefixes) {
        return new C10NModule(packagePrefixes);
    }

    public C10NModule(String... packagePrefixes) {
        this.packagePrefixes = packagePrefixes;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().filterInputsBy(getPackageInputFilter()).setUrls(getPackageURLs()));

        Set<Class<?>> c10nEnumTypes = reflections.getTypesAnnotatedWith(C10NEnumMessages.class);
        C10N.configure(new C10NConfigBase() {
            @Override
            protected void configure() {
                for (Class<?> c10nEnumType : c10nEnumTypes) {
                    C10NEnumMessages annotation = c10nEnumType.getAnnotation(C10NEnumMessages.class);
                    Class targetEnumType = annotation.value();
                    bindFilter(C10NFilters.enumMapping(targetEnumType, c10nEnumType), targetEnumType);
                }
                configured = true;
                for (C10NConfigBase config : configs) {
                    install(config);
                }
            }
        });

        Set<Class<?>> c10nTypes = reflections.getTypesAnnotatedWith(C10NMessages.class);
        for (Class<?> c10nType : c10nTypes) {
            if (c10nType.isInterface()) {
                bind((Class<Object>) c10nType).toInstance(C10N.get(c10nType));
            }
        }
    }

    private Set<URL> getPackageURLs() {
        return Arrays.asList(packagePrefixes).stream().flatMap(prefix -> ClasspathHelper.forPackage(prefix).stream())
                .collect(Collectors.toSet());
    }

    private FilterBuilder getPackageInputFilter() {
        final FilterBuilder inputFilter = new FilterBuilder();

        for (String prefix : packagePrefixes) {
            inputFilter.include(FilterBuilder.prefix(prefix));
        }

        return inputFilter;
    }

    public C10NModule addC10NConfig(C10NConfigBase... configs) {
        if (configured) {
            throw new IllegalStateException("Unale to add new configs after the moduled has already been configured.");
        }
        this.configs.addAll(Arrays.asList(configs));
        return this;
    }
}
