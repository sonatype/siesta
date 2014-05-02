package org.sonatype.siesta.webapp.test;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.ExceptionMapperSupport;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * Test {@link ExceptionMapper}.
 *
 * @since 2.0
 */
@Named
@Singleton
@Provider
public class TestExceptionMapper
  extends ExceptionMapperSupport<TestException>
{
  @Inject
  public TestExceptionMapper() {
    if (log.isTraceEnabled()) {
      log.trace("Created", new Throwable("MARKER"));
    }
    else {
      log.debug("Created");
    }
  }

  @Override
  protected Response convert(final TestException exception, final String id) {
    return Response
        .serverError()
        .entity("Error: " + exception)
        .type(TEXT_PLAIN)
        .build();
  }
}
