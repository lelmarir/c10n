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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.rodionmoiseev.c10n.C10NConfigBase;
import com.github.rodionmoiseev.c10n.C10NDef;
import com.github.rodionmoiseev.c10n.C10NEnumMessages;
import com.github.rodionmoiseev.c10n.C10NMessages;
import com.github.rodionmoiseev.c10n.annotations.DefaultC10NAnnotations;
import com.google.inject.Guice;

public class GuiceLoaderTest {

    @Test
    public void guiceTest() {
        MyGuiceMessages msg = Guice.createInjector(C10NModule.scanAllPackages().addC10NConfig(new C10NConfigBase() {
            @Override
            protected void configure() {
                install(new DefaultC10NAnnotations());
            }
        })).getInstance(MyGuiceMessages.class);
        assertThat(msg.greet(), is("Hello, Guice!"));

        assertThat(msg.iHaveAPet(Animal.CAT), is("I have a cat!"));
        assertThat(msg.iHaveAPet(Animal.DOG), is("I have a dog!"));
    }
}

enum Animal {
    CAT, DOG;
}

@C10NMessages
@C10NEnumMessages(Animal.class)
interface AnimalMessages {
    @C10NDef("cat")
    String cat();

    @C10NDef("dog")
    String dog();
}

@C10NMessages
interface MyGuiceMessages {
    @C10NDef("Hello, Guice!")
    String greet();

    @C10NDef("I have a {0}!")
    String iHaveAPet(Animal animal);
}