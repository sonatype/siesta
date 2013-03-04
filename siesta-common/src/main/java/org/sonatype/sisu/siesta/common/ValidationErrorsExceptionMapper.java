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

import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.sonatype.sisu.siesta.common.exceptions.ValidationErrorsException;
import org.sonatype.sisu.siesta.common.model.ValidationError;

/**
 * Maps {@link ValidationErrorsException} to 400 with a list of {@link ValidationError} as body.
 *
 * @since 1.4
 */
@Named
@Singleton
public class ValidationErrorsExceptionMapper
    extends ExceptionMapperSupport<ValidationErrorsException>
{

    @Override
    protected Response convert( final ValidationErrorsException exception )
    {
        return Response.status( Response.Status.BAD_REQUEST )
            .entity( new GenericEntity<List<ValidationError>>( exception.getErrors() )
            {
                @Override
                public String toString()
                {
                    return getEntity().toString();
                }
            }
            )
            .build();
    }

}