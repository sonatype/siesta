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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.sisu.siesta.common.Resource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link ComponentDiscoveryReporter} implementation.
 *
 * @since 1.3.1
 */
@Named
@Singleton
public class ComponentDiscoveryReporterImpl
    implements ComponentDiscoveryReporter
{
    protected final Logger log;

    @Inject
    public ComponentDiscoveryReporterImpl() {
        log = LoggerFactory.getLogger(getClass());
    }

    public ComponentDiscoveryReporterImpl(final Logger logger) {
        log = checkNotNull(logger);
    }

    public void report(final ComponentDiscoveryApplication application) {
        checkNotNull(application);
        reportResources(application.getResources());
        reportComponents(application.getComponents());
    }

    protected String pathOf(final Class<Resource> type) {
        Path path = type.getAnnotation(Path.class);
        return path.value();
    }

    protected void reportResources(final Set<Class<Resource>> resources) {
        if (resources.isEmpty()) {
            log.info("No resources found");
        }
        else {
            log.info("Resources:");

            // TODO: Perhaps show more details about the resource, sub-resources, methods, etc
            // TODO: Sort the resources by path before rendering so we can get all related paths grouped together

            for (Class<Resource> type : resources) {
                String path = pathOf(type);
                log.info("  {}", path);
            }
        }
    }

    protected void reportComponents(final Set<Class<?>> components) {
        if (components.isEmpty()) {
            log.info("No components found");
        }
        else {
            log.info("Components:");

            // TODO: Perhaps show various types of components (providers, mappers, etc)
            // TODO: Sort the components by full classname before rendering so we can get all related grouped together

            for (Class<?> type : components) {
                log.info("  {}", type.getSimpleName());
            }
        }
    }
}
