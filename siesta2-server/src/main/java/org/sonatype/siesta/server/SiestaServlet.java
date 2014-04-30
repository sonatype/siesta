package org.sonatype.siesta.server;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Siesta servlet.
 *
 * @since 2.0
 */
@Named
@Singleton
public class SiestaServlet
  extends ServletContainer
{
  @Inject
  public SiestaServlet(final ResourceConfig resourceConfig) {
    super(checkNotNull(resourceConfig));
  }
}
