/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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
package org.sonatype.sisu.siesta.server.internal;

import com.google.inject.Key;
import org.sonatype.sisu.siesta.common.Component;
import org.sonatype.sisu.siesta.common.Resource;
import org.sonatype.sisu.siesta.server.ApplicationSupport;
import org.sonatype.guice.bean.locators.BeanLocator;
import org.sonatype.inject.BeanEntry;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JAX-RS {@link Application} which discovers {@link Component} instances and {@link Resource} instances
 * annotated with {@link Path} automatically.
 *
 * @since 1.0
 */
public class ComponentDiscoveryApplication
    extends ApplicationSupport
{
    private final BeanLocator container;

    @Inject
    public ComponentDiscoveryApplication(final BeanLocator container) {
        this.container = checkNotNull(container);
    }

    @Override
    public Set<Class<?>> getClasses() {
        if (classes.isEmpty()) {
            findComponents();
        }
        return super.getClasses();
    }

    private void findComponents() {
        log.debug("Finding components");

        for (BeanEntry<Annotation, Component> entry : container.locate(Key.get(Component.class))) {
            Class<?> type = entry.getImplementationClass();
            if (Resource.class.isAssignableFrom(type)) {
                // only add resources which are annotated with a path
                Path path = type.getAnnotation(Path.class);
                if (path != null) {
                    log.debug("Adding resource: {} -> {}", path.value(), type);
                    classes.add(type);
                }
            }
            else {
                log.debug("Adding component: {}", type);
                classes.add(type);
            }
        }

        log.debug("Found {} components", classes.size());
    }
}
