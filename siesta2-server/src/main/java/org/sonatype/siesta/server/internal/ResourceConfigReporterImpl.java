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

package org.sonatype.siesta.server.internal;

import java.util.Collections;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.Path;

import org.sonatype.siesta.Component;
import org.sonatype.siesta.Resource;
import org.sonatype.siesta.server.ResourceConfigReporter;

import com.google.common.collect.Lists;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link ResourceConfigReporter} implementation.
 *
 * @since 2.0
 */
@Named
public class ResourceConfigReporterImpl
    implements ResourceConfigReporter
{
  protected final Logger log = LoggerFactory.getLogger(getClass());

  public void report(final ResourceConfig config) {
    checkNotNull(config);

    log.debug("Configuration: {}", config);
    log.debug("Classes: {}", config.getClasses());
    log.debug("Instances: {}", config.getInstances());
    log.debug("Singletons: {}", config.getSingletons());

    reportResources(config);
    reportComponents(config);
  }

  protected void reportResources(final ResourceConfig config) {
    List<String> paths = Lists.newArrayList();

    for (Class<?> type : config.getClasses()) {
      if (Resource.class.isAssignableFrom(type)) {
        Path path = type.getAnnotation(Path.class);
        if (path != null) {
          paths.add(path.value());
        }
      }
    }

    if (paths.isEmpty()) {
      log.info("No resources found");
    }
    else {
      Collections.sort(paths);

      log.info("Resources:");
      for (String path : paths) {
        log.info("  {}", path);
      }
    }
  }

  protected void reportComponents(final ResourceConfig config) {
    List<String> names = Lists.newArrayList();

    for (Class<?> type : config.getClasses()) {
      if (Component.class.isAssignableFrom(type) && !Resource.class.isAssignableFrom(type)) {
        names.add(type.getName());
      }
    }

    if (names.isEmpty()) {
      log.info("No components found");
    }
    else {
      Collections.sort(names);

      log.info("Components:");
      for (String name : names) {
        log.info("  {}", name);
      }
    }
  }
}
