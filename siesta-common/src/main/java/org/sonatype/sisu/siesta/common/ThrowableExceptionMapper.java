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
package org.sonatype.sisu.siesta.common;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.google.common.base.Preconditions;

/**
 * Ensures that any exceptions internally created by the JAX-RS container have a plain text body with the error message.
 *
 * @since 1.4
 */
@Named
@Singleton
public class ThrowableExceptionMapper
    extends ExceptionMapperSupport<Throwable>
    implements ExceptionMapper<Throwable>
{

    private ErrorResponseGenerator errorResponseGenerator;

    @Inject
    public ThrowableExceptionMapper( final ErrorResponseGenerator errorResponseGenerator )
    {
        this.errorResponseGenerator = checkNotNull( errorResponseGenerator );
    }

    protected Response convert( Throwable e )
    {
        final ErrorResponse response = errorResponseGenerator.mapException( e );

        return Response.status( response.getStatusCode() )
            .type( ErrorResponse.CONTENT_TYPE )
            .entity( response.getMessageBody() )
            .build();
    }

}
