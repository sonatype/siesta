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

package org.sonatype.siesta.server;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.RuntimeDelegate;

import org.sonatype.siesta.Component;

import com.google.inject.Key;
import org.eclipse.sisu.BeanEntry;
import org.eclipse.sisu.Mediator;
import org.eclipse.sisu.inject.BeanLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Siesta servlet.
 *
 * This is a thin wrapper around {@link ComponentContainer} which also handles {@link Component} registration.
 *
 * @since 2.0
 */
@Named
@Singleton
public class SiestaServlet
    extends HttpServlet
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final BeanLocator beanLocator;

  private final ComponentContainer componentContainer;

  @Inject
  public SiestaServlet(final BeanLocator beanLocator, final ComponentContainer componentContainer) {
    this.beanLocator = checkNotNull(beanLocator);
    this.componentContainer = checkNotNull(componentContainer);

    log.debug("Component container: {}", componentContainer);
  }

  @Override
  public void init(final ServletConfig config) throws ServletException {
    final ClassLoader cl = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      doInit(config);
    }
    finally {
      Thread.currentThread().setContextClassLoader(cl);
    }
  }

  private void doInit(final ServletConfig config) throws ServletException {
    super.init(config);

    // TODO: Figure out what version of RESTEasy is used and log it

    // Initialize container
    componentContainer.init(config);
    log.info("JAX-RS RuntimeDelegate: {}", RuntimeDelegate.getInstance());

    // Watch for components
    beanLocator.watch(Key.get(Component.class), new ComponentMediator(), componentContainer);

    log.info("Initialized");
  }

  /**
   * Handles component [de]registration events.
   */
  private class ComponentMediator
      implements Mediator<Annotation, Component, ComponentContainer>
  {
    @Override
    public void add(final BeanEntry<Annotation, Component> entry, final ComponentContainer container) throws Exception {
      log.debug("Adding component: {}", entry.getKey());
      try {
        container.addComponent(entry);
      }
      catch (Exception e) {
        log.error("Failed to add component", e);
      }
    }

    @Override
    public void remove(final BeanEntry<Annotation, Component> entry, final ComponentContainer container)
        throws Exception
    {
      log.debug("Removing component: {}", entry.getKey());
      try {
        container.removeComponent(entry);
      }
      catch (Exception e) {
        log.error("Failed to remove component", e);
      }
    }
  }

  @Override
  public void service(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException
  {
    checkNotNull(request);
    checkNotNull(response);

    // Log the request URI+URL muck
    String uri = request.getRequestURI();
    if (request.getQueryString() != null) {
      uri = String.format("%s?%s", uri, request.getQueryString());
    }

    if (log.isDebugEnabled()) {
      log.debug("Processing: {} {} ({})", request.getMethod(), uri, request.getRequestURL());
    }

    if (log.isTraceEnabled()) {
      log.trace("Context path: {}", request.getContextPath());
      log.trace("Servlet path: {}", request.getServletPath());
    }

    MDC.put(getClass().getName(), uri);
    try {
      componentContainer.service(request, response);
    }
    finally {
      MDC.remove(getClass().getName());
    }
  }

  @Override
  public void destroy() {
    componentContainer.destroy();
    super.destroy();

    log.info("Destroyed");
  }
}
