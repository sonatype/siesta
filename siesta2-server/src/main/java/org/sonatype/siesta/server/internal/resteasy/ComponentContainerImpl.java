package org.sonatype.siesta.server.internal.resteasy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sonatype.siesta.Resource;
import org.sonatype.siesta.server.ComponentContainer;

import org.eclipse.sisu.BeanEntry;
import org.jboss.resteasy.logging.Logger.LoggerType;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

/**
 * RESTEasy {@link ComponentContainer}
 */
public class ComponentContainerImpl
  extends HttpServletDispatcher
  implements ComponentContainer
{
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
    }
    else {
      // TODO: Doesn't seem to be a late-biding/factory here so we create the object early
      getDispatcher().getProviderFactory().register(entry.getValue());
    }
  }

  @Override
  public void removeComponent(final BeanEntry<?, ?> entry) throws Exception {
    if (isResource(entry)) {
      getDispatcher().getRegistry().removeRegistrations(entry.getImplementationClass());
    }
    else {
      // TODO: Unsure how to remove a component
    }
  }
}
