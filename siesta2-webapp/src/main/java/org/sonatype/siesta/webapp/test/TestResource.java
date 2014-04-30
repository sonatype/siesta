package org.sonatype.siesta.webapp.test;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.sonatype.siesta.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test resource.
 *
 * @since 2.0
 */
@Named
@Singleton
@Path("/test")
public class TestResource
  implements Resource
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  public TestResource() {
    // So we can see if guice or hk2 created the instance
    log.trace("Created", new Throwable("MARKER"));
  }

  @GET
  @Produces("text/plain")
  public String get() {
    return "Hello";
  }

  @GET
  @Path("ping")
  @Produces("text/plain")
  public String get(final @QueryParam("text") @DefaultValue("pong") String text) {
    return text;
  }

  @GET
  @Path("error")
  @Produces("text/plain")
  public String error(final @QueryParam("text") String text) throws Exception {
    throw new TestException(text);
  }
}
