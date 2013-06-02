/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
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

import com.google.common.collect.Sets;
import com.google.inject.Key;
import org.sonatype.guice.bean.locators.BeanLocator;
import org.sonatype.inject.BeanEntry;
import org.sonatype.sisu.siesta.common.Component;
import org.sonatype.sisu.siesta.common.Resource;
import org.sonatype.sisu.siesta.server.ApplicationSupport;

import javax.annotation.Nullable;
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

    private final Set<Class<Resource>> resources = Sets.newHashSet();

    private final Set<Class<?>> components = Sets.newHashSet();

    private final ComponentDiscoveryReporter reporter;

    @Inject
    public ComponentDiscoveryApplication(final BeanLocator container,
                                         final @Nullable ComponentDiscoveryReporter reporter)
    {
        this.container = checkNotNull(container);
        this.reporter = reporter;

        if (reporter != null) {
            log.debug("Reporter: {}", reporter);
        }
    }

    public Set<Class<Resource>> getResources() {
        return resources;
    }

    public Set<Class<?>> getComponents() {
        return components;
    }

    @Override
    public Set<Class<?>> getClasses() {
        if (classes.isEmpty()) {
            findComponents();

            // If a reporter is configured, then ask it to report on what we discovered
            if (reporter != null) {
                reporter.report(this);
            }
        }
        return super.getClasses();
    }

    private void findComponents() {
        log.debug("Finding components");

        for (BeanEntry<Annotation, Component> entry : container.locate(Key.get(Component.class))) {
            log.trace("Found: {}", entry);

            Class<?> type = entry.getImplementationClass();
            if (Resource.class.isAssignableFrom(type)) {
                // only add resources which are annotated with a path
                Path path = type.getAnnotation(Path.class);
                if (path != null) {
                    log.debug("Adding resource: {} -> {}", path.value(), type);
                    //noinspection unchecked
                    resources.add((Class<Resource>) type);
                }
            }
            else {
                log.debug("Adding component: {}", type);
                components.add(type);
            }
        }

        classes.addAll(resources);
        classes.addAll(components);

        // TODO: Support singleton component discovery

        log.debug("Found {} components", classes.size());
    }
}
