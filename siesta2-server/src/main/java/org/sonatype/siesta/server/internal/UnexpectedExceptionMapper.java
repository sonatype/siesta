package org.sonatype.siesta.server.internal;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.ExceptionMapperSupport;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * Unexpected generic {@link Throwable} exception mapper.
 *
 * This will handle all unexpected exceptions and override the default JAX-RS implementations behavior.
 *
 * @since 2.0
 */
@Named
@Singleton
@Provider
public class UnexpectedExceptionMapper
  extends ExceptionMapperSupport<Throwable>
{
  @Override
  protected Response convert(final Throwable exception, final String id) {
    // always log unexpected exception with stack
    log.warn("(ID {}) Unexpected exception: {}", id, exception.toString(), exception);

    return Response.serverError()
        .entity(String.format("ERROR: (ID %s) %s", id, exception))
        .type(TEXT_PLAIN)
        .build();
  }
}
