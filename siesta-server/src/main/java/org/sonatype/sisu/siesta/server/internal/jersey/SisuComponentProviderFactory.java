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

import com.google.inject.Key;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProvider;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.core.spi.component.ioc.IoCInstantiatedComponentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.guice.bean.locators.BeanLocator;
import org.sonatype.inject.BeanEntry;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides IoC support for loading Jersey components via Sisu's {@link BeanLocator}.
 *
 * @since 1.0
 */
@Named
public class SisuComponentProviderFactory
    implements IoCComponentProviderFactory
{
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final BeanLocator container;

    @Inject
    public SisuComponentProviderFactory(final BeanLocator container) {
        this.container = checkNotNull(container);
        log.debug("Container: {}", container);
    }

    public IoCComponentProvider getComponentProvider(final Class<?> type) {
        checkNotNull(type);

        log.trace("Get component provider: {}", type);

        @SuppressWarnings({"unchecked"}) // dropping <?> from type, it can cause compile errors due to incompatible types from capture#
        Iterator<BeanEntry<Annotation, ?>> iter = container.locate(Key.get((Class)type)).iterator();
        if (iter.hasNext()) {
            final BeanEntry entry = iter.next();
            log.trace("Found component: {}", entry);

            return new IoCInstantiatedComponentProvider()
            {
                public Object getInjectableInstance(final Object obj) {
                    return obj;
                }

                public Object getInstance() {
                    return entry.getValue();
                }
            };
        }

        return null;
    }

    public IoCComponentProvider getComponentProvider(final ComponentContext context, final Class<?> type) {
        return getComponentProvider(type);
    }
}
