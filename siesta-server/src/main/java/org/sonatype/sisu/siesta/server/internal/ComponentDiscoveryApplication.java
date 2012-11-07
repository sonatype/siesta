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

import com.google.common.collect.Sets;
import com.google.inject.Key;
import org.sonatype.guice.bean.locators.BeanLocator;
import org.sonatype.inject.BeanEntry;
import org.sonatype.sisu.siesta.common.Component;
import org.sonatype.sisu.siesta.common.Resource;
import org.sonatype.sisu.siesta.server.ApplicationSupport;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.lang.annotation.Annotation;
import java.util.List;
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
    private static final String CPREFIX = "${org.sonatype.sisu.siesta.server.internal.ComponentDiscoveryApplication";

    private final BeanLocator container;

    private final Set<Class<Resource>> resources = Sets.newHashSet();

    private final Set<Class<?>> components = Sets.newHashSet();

    private boolean report;

    @Inject
    public ComponentDiscoveryApplication(final BeanLocator container, final @Named(CPREFIX + ".report:-true}") boolean report) {
        this.container = checkNotNull(container);
        this.report = report;
        log.debug("Report: {}", report);
    }

    @Override
    public Set<Class<?>> getClasses() {
        if (classes.isEmpty()) {
            findComponents();
            if (report) {
                displayReport();
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

    private void displayReport() {
        displayResourceReport();
        displayNonResourceReport();
    }

    // TODO: Allow report logging level to be customized

    private void displayResourceReport() {
        if (resources.isEmpty()) {
            log.info("No resources found");
        }
        else {
            log.info("Resources:");
            for (Class<Resource> type : resources) {
                // TODO: Perhaps show more details about the resource, sub-resources, methods, etc
                Path path = type.getAnnotation(Path.class);
                log.info("  {}", path.value());
            }
        }
    }

    private void displayNonResourceReport() {
        if (components.isEmpty()) {
            log.info("No components found");
        }
        else {
            log.info("Components:");
            // TODO: Perhaps show various types of components (providers, mappers, etc)
            for (Class<?> type : components) {
                log.info("  {}", type.getSimpleName());
            }
        }
    }
}
