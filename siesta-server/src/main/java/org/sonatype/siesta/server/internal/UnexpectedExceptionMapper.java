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
