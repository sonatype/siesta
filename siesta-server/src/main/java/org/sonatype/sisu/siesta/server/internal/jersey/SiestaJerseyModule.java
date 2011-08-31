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
package org.sonatype.sisu.siesta.server.internal.jersey;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.sonatype.sisu.siesta.server.ApplicationContainer;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;

import javax.inject.Singleton;

/**
 * Siesta Jersey Guice module.
 *
 * @since 1.0
 */
public class SiestaJerseyModule
    implements Module
{
    public void configure(final Binder binder) {
        binder.bind(ApplicationContainer.class).to(JerseyContainer.class).in(Singleton.class);
        binder.bind(IoCComponentProviderFactory.class).to(SisuComponentProviderFactory.class).in(Singleton.class);
    }
}
