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
package org.sonatype.siesta.server.internal.resteasy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.sonatype.siesta.Resource;
import org.sonatype.siesta.server.ComponentContainer;

import org.eclipse.sisu.BeanEntry;
import org.jboss.resteasy.logging.Logger.LoggerType;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RESTEasy {@link ComponentContainer}
 */
public class ComponentContainerImpl
  extends HttpServletDispatcher
  implements ComponentContainer
{
  private static final Logger log = LoggerFactory.getLogger(ComponentContainerImpl.class);

  public ComponentContainerImpl() {
    // Configure RESTEasy to use SLF4j
    org.jboss.resteasy.logging.Logger.setLoggerType(LoggerType.SLF4J);
  }

  @Override
  public void service(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException
  {
    super.service(request, response);
  }

  private boolean isResource(final BeanEntry<?, ?> entry) {
    return Resource.class.isAssignableFrom(entry.getImplementationClass());
  }

  @Override
  public void addComponent(final BeanEntry<?, ?> entry) throws Exception {
    if (isResource(entry)) {
      getDispatcher().getRegistry().addResourceFactory(new SisuResourceFactory(entry));
      Class<?> type = entry.getImplementationClass();
      Path path = type.getAnnotation(Path.class);
      if (path == null) {
        log.warn("Found resource w/o @Path: {}", type.getName());
      }
      else {
        log.debug("Added resource: {} with path: {}", type.getName(), path.value());
      }
    }
    else {
      // TODO: Doesn't seem to be a late-biding/factory here so we create the object early
      getDispatcher().getProviderFactory().register(entry.getValue());
      log.debug("Added component: {}", entry.getImplementationClass());
    }
  }

  @Override
  public void removeComponent(final BeanEntry<?, ?> entry) throws Exception {
    if (isResource(entry)) {
      getDispatcher().getRegistry().removeRegistrations(entry.getImplementationClass());
      log.debug("Added component: {}", entry.getImplementationClass());
    }
    else {
      // TODO: Unsure how to remove a component
      log.warn("Unable to remove component: {}", entry.getImplementationClass());
    }
  }
}
