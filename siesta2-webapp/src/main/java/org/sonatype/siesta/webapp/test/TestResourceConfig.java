package org.sonatype.siesta.webapp.test;

import javax.inject.Named;
import javax.inject.Singleton;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Test {@link ResourceConfig}.
 *
 * @since 2.0
 */
@Named
@Singleton
public class TestResourceConfig
  extends ResourceConfig
{
  public TestResourceConfig() {
    register(TestResource.class);
  }
}
