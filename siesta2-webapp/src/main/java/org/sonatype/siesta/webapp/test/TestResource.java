/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.siesta.webapp.test;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.sonatype.siesta.Resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jboss.resteasy.core.Dispatcher;
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

  @Inject
  public TestResource() {
    if (log.isTraceEnabled()) {
      log.trace("Created", new Throwable("MARKER"));
    }
    else {
      log.debug("Created");
    }
  }

  @Inject
  public void testInjection(final Dispatcher dispatcher) {
    log.info("Dispatcher: {}", dispatcher);
  }

  @GET
  @Produces(TEXT_PLAIN)
  public String get() {
    return "Hello";
  }

  @GET
  @Path("ping")
  @Produces(TEXT_PLAIN)
  public String ping(final @QueryParam("text") @DefaultValue("pong") String text) {
    if (log.isTraceEnabled()) {
      log.trace("PING", new Throwable("MARKER"));
    }
    else {
      log.debug("PING");
    }

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

    @JsonProperty("date")
    Date date = new Date();
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
