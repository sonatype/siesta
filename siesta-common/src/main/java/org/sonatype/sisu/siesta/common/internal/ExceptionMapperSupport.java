/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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
package org.sonatype.sisu.siesta.common.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.sisu.siesta.common.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Support for {@link ExceptionMapper} implementations.
 *
 * @since 1.0
 */
public abstract class ExceptionMapperSupport<E extends Throwable>
    implements Component, ExceptionMapper<E>
{
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public Response toResponse(final E exception) {
        //noinspection ThrowableResultOfMethodCallIgnored
        checkNotNull(exception);

        if (log.isTraceEnabled()) {
            log.trace("Mapping exception: " + exception, exception);
        }
        else {
            log.debug("Mapping exception: " + exception);
        }

        Response response = convert(exception);
        log.debug("Response: [{}] {}", response.getStatus(), response.getEntity());

        return response;
    }

    protected abstract Response convert(final E exception);
}
