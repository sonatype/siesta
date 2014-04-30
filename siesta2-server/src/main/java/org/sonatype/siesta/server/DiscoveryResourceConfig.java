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

package org.sonatype.siesta.server;

import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.sonatype.siesta.Component;
import org.sonatype.siesta.Resource;

import com.google.inject.Key;
import org.eclipse.sisu.BeanEntry;
import org.eclipse.sisu.inject.BeanLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JAX-RS {@link Application} which discovers {@link Component} instances and {@link Resource} instances
 * annotated with {@link Path} automatically.
 *
 * @since 2.0
 */
@Named
public class DiscoveryResourceConfig
    extends ResourceConfig
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final BeanLocator beanLocator;

  @Inject
  public DiscoveryResourceConfig(final BeanLocator beanLocator) {
    this.beanLocator = checkNotNull(beanLocator);
    findComponents();
  }

  private void findComponents() {
    log.debug("Finding components");

    int count=0;

    for (BeanEntry<Annotation, Component> entry : beanLocator.locate(Key.get(Component.class))) {
      log.trace("Found: {}", entry.getKey());

      Class<?> type = entry.getImplementationClass();
      if (Resource.class.isAssignableFrom(type)) {
        // only add resources which are annotated with a path
        Path path = type.getAnnotation(Path.class);
        if (path != null) {
          log.debug("Adding resource: {} -> {}", path.value(), type);
          register(type);
          count++;
        }
      }
      else {
        log.debug("Adding component: {}", type);
        register(type);
        count++;
      }
    }

    log.debug("Found {} components", count);
  }
}
