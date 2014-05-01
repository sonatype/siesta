package org.sonatype.siesta.webapp.test;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.sonatype.siesta.Resource;

import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

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
    log.trace("Created", new Throwable("MARKER"));
  }

  @GET
  @Produces(TEXT_PLAIN)
  public String get() {
    return "Hello";
  }

  @GET
  @Path("ping")
  @Produces(TEXT_PLAIN)
  public String get(final @QueryParam("text") @DefaultValue("pong") String text) {
    return text;
  }

  @GET
  @Path("error")
  @Produces(TEXT_PLAIN)
  public String error(final @QueryParam("text") String text) throws Exception {
    throw new TestException(text);
  }

  public static class JsonObject
  {
    @JsonProperty("foo")
    String foo;

    @JsonProperty("bar")
    String bar;
  }

  @GET
  @Path("json")
  @Produces(APPLICATION_JSON)
  public JsonObject json() {
    JsonObject result = new JsonObject();
    result.foo = "hi";
    result.bar = "there";
    return result;
  }
}
