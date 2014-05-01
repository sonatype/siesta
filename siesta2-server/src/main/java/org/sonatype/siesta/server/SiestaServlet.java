package org.sonatype.siesta.server;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sonatype.siesta.Component;
import org.sonatype.siesta.Resource;

import com.google.inject.Key;
import org.eclipse.sisu.BeanEntry;
import org.eclipse.sisu.Mediator;
import org.eclipse.sisu.inject.BeanLocator;
import org.jboss.resteasy.logging.Logger.LoggerType;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Siesta servlet.
 *
 * @since 2.0
 */
@Named
@Singleton
public class SiestaServlet
  extends HttpServletDispatcher
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final BeanLocator beanLocator;

  @Inject
  public SiestaServlet(final BeanLocator beanLocator) {
    this.beanLocator = checkNotNull(beanLocator);

    // Configure RESTEasy to use SLF4j
    org.jboss.resteasy.logging.Logger.setLoggerType(LoggerType.SLF4J);
  }

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);

    // Watch for components
    beanLocator.watch(Key.get(Component.class), new ComponentMediator(), this);
  }

  /**
   * Handles component [de]registration events.
   */
  private class ComponentMediator
      implements Mediator<Annotation, Component, SiestaServlet>
  {
    @Override
    public void add(final BeanEntry<Annotation, Component> entry, final SiestaServlet watcher) throws Exception {
      log.debug("Adding component: {}={}", entry.getKey(), entry.getImplementationClass());
      try {
        if (Resource.class.isAssignableFrom(entry.getImplementationClass())) {
          getDispatcher().getRegistry().addResourceFactory(new SisuResourceFactory(entry));
        }
        else {
          // TODO: Doesn't seem to be a late-biding/factory here so we create the object early
          getDispatcher().getProviderFactory().register(entry.getValue());
        }
      }
      catch (Exception e) {
        log.error("Failed to add component", e);
      }
    }

    @Override
    public void remove(final BeanEntry<Annotation, Component> entry, final SiestaServlet watcher) throws Exception {
      log.debug("Removing component: {}={}", entry.getKey(), entry.getImplementationClass());
      try {
        if (Resource.class.isAssignableFrom(entry.getImplementationClass())) {
          getDispatcher().getRegistry().removeRegistrations(entry.getImplementationClass());
        }
        else {
          // TODO: Unsure how to remove a component
        }
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

    MDC.put(getClass().getName(), uri);
    try {
      super.service(request, response);
    }
    finally {
      MDC.remove(getClass().getName());
    }
  }
}
