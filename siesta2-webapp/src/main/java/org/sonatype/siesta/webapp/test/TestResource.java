package org.sonatype.siesta.webapp.test;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Test resource.
 *
 * @since 2.0
 */
@Named
@Singleton
@Path("/test")
public class TestResource
{
  public TestResource() {
    // So we can see if guice or hk2 created the instance
    new Throwable("CREATE MARKER").printStackTrace();
  }

  @GET
  @Produces("text/plain")
  public String get(final @QueryParam("text") String text) {
    return "Hello: " + text;
  }
}
