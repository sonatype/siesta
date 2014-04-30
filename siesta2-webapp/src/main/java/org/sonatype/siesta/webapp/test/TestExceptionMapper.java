package org.sonatype.siesta.webapp.test;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test {@link ExceptionMapper}.
 *
 * @since 2.0
 */
@Named
@Singleton
@Provider
public class TestExceptionMapper
  implements ExceptionMapper<TestException>, Component
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public Response toResponse(final TestException exception) {
    log.debug("Mapping: " + exception);
    return Response
        .serverError()
        .entity("Error: " + exception)
        .type("text/plain")
        .build();
  }
}
