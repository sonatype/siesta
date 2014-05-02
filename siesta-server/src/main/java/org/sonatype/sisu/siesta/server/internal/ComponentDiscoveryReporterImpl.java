/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;

import org.sonatype.sisu.siesta.common.Resource;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

      List<String> paths = Lists.newArrayListWithCapacity(resources.size());
      for (Class<Resource> type : resources) {
        String path = pathOf(type);
        paths.add(path);
      }

      Collections.sort(paths);
      for (String path : paths) {
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

      List<String> types = Lists.newArrayListWithCapacity(components.size());
      for (Class<?> type : components) {
        types.add(type.getName());
      }

      Collections.sort(types);

      for (String type : types) {
        log.info("  {}", type);
      }
    }
  }
}
