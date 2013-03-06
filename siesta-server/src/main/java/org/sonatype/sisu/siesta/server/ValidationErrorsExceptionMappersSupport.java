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

import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_VALIDATION_ERRORS_V1_JSON_TYPE;
import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_VALIDATION_ERRORS_V1_XML_TYPE;

import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;

import org.sonatype.sisu.siesta.common.validation.ValidationErrorXO;

/**
 * Support calls for exception mappers returning 400 with {@link ValidationErrorXO}s in body.
 *
 * @since 1.4
 */
public abstract class ValidationErrorsExceptionMappersSupport<E extends Throwable>
    extends ExceptionMapperSupport<E>
{

    private final List<Variant> variants_v1;

    public ValidationErrorsExceptionMappersSupport()
    {
        variants_v1 = Variant.mediaTypes(
            VND_VALIDATION_ERRORS_V1_JSON_TYPE, VND_VALIDATION_ERRORS_V1_XML_TYPE
        ).add().build();
    }

    @Override
    protected Response convert( final E exception, final String id )
    {
        final Response.ResponseBuilder builder = Response.status( getStatusCode( exception ) );

        final List<ValidationErrorXO> validationErrors = getValidationErrors( exception );
        if ( validationErrors != null && !validationErrors.isEmpty() )
        {
            final Variant variant_v1 = getRequest().selectVariant( variants_v1 );
            if ( variant_v1 != null )
            {
                builder
                    .type( variant_v1.getMediaType() )
                    .entity(
                        new GenericEntity<List<ValidationErrorXO>>( validationErrors )
                        {
                            @Override
                            public String toString()
                            {
                                return getEntity().toString();
                            }
                        }
                    );
            }
        }

        return builder.build();
    }

    protected int getStatusCode( final E exception )
    {
        return Response.Status.BAD_REQUEST.getStatusCode();
    }

    protected abstract List<ValidationErrorXO> getValidationErrors( final E exception );

}