package org.sonatype.siesta.webapp;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.siesta.server.ResourceConfigReporter;
import org.sonatype.siesta.server.SiestaServlet;
import org.sonatype.siesta.server.DiscoveryResourceConfig;
import org.sonatype.siesta.server.internal.ResourceConfigReporterImpl;
import org.sonatype.siesta.webapp.test.TestApplication;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;
import org.glassfish.jersey.server.ResourceConfig;

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
    bind(ResourceConfig.class).to(TestApplication.class).in(Singleton.class);
    bind(ResourceConfigReporter.class).to(ResourceConfigReporterImpl.class).in(Singleton.class);

    install(new ServletModule()
    {
      @Override
      protected void configureServlets() {
        serve("/*").with(SiestaServlet.class);
      }
    });
  }
}
