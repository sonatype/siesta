package org.sonatype.siesta.server.internal;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.ExceptionMapperSupport;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * Generic {@link Throwable} exception mapper.
 *
 * @since 2.0
 */
@Named
@Singleton
@Provider
public class GenericExceptionMapper
  extends ExceptionMapperSupport<Throwable>
{
  @Override
  protected Response convert(final Throwable exception, final String id) {
    // always log unexpected exception with stack
    log.warn("(ID {}) {}", id, exception.toString(), exception);

    return Response.serverError()
        .entity(exception.toString())
        .type(TEXT_PLAIN)
        .build();
  }
}
