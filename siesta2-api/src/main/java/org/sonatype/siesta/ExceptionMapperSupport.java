/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
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

package org.sonatype.siesta;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support for {@link ExceptionMapper} implementations.
 *
 * @since 2.0
 */
public abstract class ExceptionMapperSupport<E extends Throwable>
    implements ExceptionMapper<E>, Component
{
  protected final Logger log = LoggerFactory.getLogger(getClass());

  public Response toResponse(final E exception) {
    if (exception == null) {
      throw new NullPointerException();
    }

    // Generate unique identifier
    final String id = FaultIdGenerator.generate();

    // debug/trace log exception details
    if (log.isTraceEnabled()) {
      log.trace("(ID {}) Mapping exception: " + exception, id, exception);
    }
    else {
      log.debug("(ID {}) Mapping exception: " + exception, id);
    }

    // Prepare the response
    Response response;
    try {
      response = convert(exception, id);
    }
    catch (Exception e) {
      log.warn("(ID {}) Failed to map exception", id, e);
      response = Response.serverError().entity(new FaultXO(id, e)).build();
    }

    // Log terse (unless debug enabled) warning with fault details
    final Object entity = response.getEntity();
    log.warn("(ID {}) Response: [{}] {}; mapped from: {}",
        id,
        response.getStatus(),
        entity == null ? "(no entity/body)" : String.format("'%s'", entity),
        exception,
        log.isDebugEnabled() ? exception : null
    );

    return response;
  }

  /**
   * Convert the given exception into a response.
   *
   * @param exception The exception to convert.
   * @param id        The unique identifier generated for this fault.
   */
  protected abstract Response convert(final E exception, final String id);
}
