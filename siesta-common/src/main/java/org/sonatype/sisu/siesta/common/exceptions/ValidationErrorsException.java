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
package org.sonatype.sisu.siesta.common.exceptions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;

import org.sonatype.sisu.siesta.common.model.ValidationError;
import com.google.common.collect.Lists;

/**
 * Thrown when a resource request is invalid.
 *
 * @since 1.4
 */
public class ValidationErrorsException
    extends RuntimeException
{

    private final List<ValidationError> errors = Lists.newArrayList();

    public ValidationErrorsException()
    {
        super();
    }

    public ValidationErrorsException( final String message )
    {
        super( message );
        errors.add( new ValidationError( message ) );
    }

    public ValidationErrorsException( final String path, final String message )
    {
        super( message );
        errors.add( new ValidationError( path, message ) );
    }

    public ValidationErrorsException withError( final String message )
    {
        errors.add( new ValidationError( message ) );
        return this;
    }

    public ValidationErrorsException withError( final String path, final String message )
    {
        errors.add( new ValidationError( path, message ) );
        return this;
    }

    public ValidationErrorsException withErrors( final ValidationError... validationErrors )
    {
        errors.addAll( Arrays.asList( checkNotNull( validationErrors ) ) );
        return this;
    }

    public List<ValidationError> getErrors()
    {
        return errors;
    }

}
