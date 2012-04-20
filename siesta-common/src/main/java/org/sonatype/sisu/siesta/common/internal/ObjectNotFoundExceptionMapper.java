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

import org.sonatype.sisu.siesta.common.ObjectNotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Converts {@link ObjectNotFoundException} to a JAX-RS {@link Response} with {@link Status#NOT_FOUND}.
 *
 * @since 1.0
 */
@Named
@Singleton
public class ObjectNotFoundExceptionMapper
    extends ExceptionMapperSupport<ObjectNotFoundException>
{
    @Override
    protected Response convert(final ObjectNotFoundException exception) {
        return Response.status(NOT_FOUND).entity(exception.getMessage()).build();
    }
}
