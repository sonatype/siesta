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
package org.sonatype.sisu.siesta.server;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.sisu.siesta.common.Component;
import org.sonatype.sisu.siesta.common.exceptions.ErrorXO;

/**
 * Support for {@link ExceptionMapper} implementations.
 *
 * @since 1.0
 */
public abstract class ExceptionMapperSupport<E extends Throwable>
    implements Component, ExceptionMapper<E>
{

    protected final Logger log = LoggerFactory.getLogger( getClass() );

    public Response toResponse( final E exception )
    {
        //noinspection ThrowableResultOfMethodCallIgnored
        checkNotNull( exception );

        if ( log.isTraceEnabled() )
        {
            log.trace( "Mapping exception: " + exception, exception );
        }
        else
        {
            log.debug( "Mapping exception: " + exception );
        }

        final String uuid = UUID.randomUUID().toString();
        Response response = convert( exception, uuid );
        log.warn( "Response (UUID {}): [{}] {}", uuid, response.getStatus(), response.getEntity() );

        return response;
    }

    protected Response convert( final E exception, final String id )
    {
        return Response.status( getStatusCode( exception ) )
            .entity( new ErrorXO().withId( id ).withMessage( getMessage( exception ) ) )
            .build();
    }

    protected String getMessage( final E exception )
    {
        return exception.getMessage();
    }

    protected int getStatusCode( final E exception )
    {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

}