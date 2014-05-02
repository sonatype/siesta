package org.sonatype.siesta.webapp;

import javax.inject.Named;

import org.sonatype.siesta.server.SiestaServlet;
import org.sonatype.siesta.server.internal.resteasy.ResteasyModule;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;

/**
 * Webapp module.
 *
 * @since 2.0
 */
@Named
public class WebappModule
    extends AbstractModule
{
  @Override
  protected void configure() {
    install(new ResteasyModule());

    install(new ServletModule()
    {
      @Override
      protected void configureServlets() {
        serve("/*").with(SiestaServlet.class);
      }
    });
  }
}
