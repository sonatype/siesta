package org.sonatype.siesta.webapp.test;

import org.sonatype.siesta.server.DiscoveryResourceConfig;

import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * ???
 *          [
 * @since 2.0
 */
public class TestApplication
   extends DiscoveryResourceConfig
{
  public TestApplication() {
    register(JacksonFeature.class);
  }
}
