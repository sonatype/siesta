/*
 * Copyright (c) 2007-2011 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.sisu.siesta.client.internal;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.sonatype.sisu.siesta.client.ClientProxyFactory;

import javax.inject.Singleton;

/**
 * Siesta Client Guice module.
 *
 * @since 1.0
 */
public class SiestaClientModule
    implements Module
{
    public void configure(final Binder binder) {
        binder.bind(ClientProxyFactory.class).to(ClientProxyFactoryImpl.class).in(Singleton.class);
    }
}
