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

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.sonatype.guice.bean.locators.BeanLocator;
import org.sonatype.inject.BeanEntry;
import org.sonatype.sisu.siesta.common.Resource;
import org.sonatype.sisu.siesta.server.ApplicationSupport;

import com.google.inject.Key;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JAX-RS {@link Application} which discovers {@link Resource} instances annotated with {@link Path} automatically.
 *
 * @since 1.0
 */
public class ResourceDiscoveryApplication
    extends ApplicationSupport
{
  private final BeanLocator container;

  @Inject
  public ResourceDiscoveryApplication(final BeanLocator container) {
    this.container = checkNotNull(container);
  }

  @Override
  public Set<Class<?>> getClasses() {
    if (classes.isEmpty()) {
      findResources();
    }
    return super.getClasses();
  }

  private void findResources() {
    log.debug("Finding resources");

    for (BeanEntry<Annotation, Resource> entry : container.locate(Key.get(Resource.class))) {
      Class<?> type = entry.getImplementationClass();
      Path path = type.getAnnotation(Path.class);
      if (path != null) {
        log.debug("Adding resource: {} -> {}", path.value(), type);
        classes.add(type);
      }
    }

    log.debug("Found {} resources", classes.size());
  }
}
