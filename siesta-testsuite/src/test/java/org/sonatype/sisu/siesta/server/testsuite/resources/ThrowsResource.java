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
package org.sonatype.sisu.siesta.server.testsuite.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.sisu.siesta.common.Resource;
import org.sonatype.sisu.siesta.common.exceptions.BadRequestException;
import org.sonatype.sisu.siesta.common.exceptions.ObjectNotFoundException;

/**
 * @since 2.4
 */
@Named
@Singleton
@Path( "/throw" )
public class ThrowsResource
    implements Resource
{

    private final Logger log = LoggerFactory.getLogger( getClass() );

    @GET
    @Path( "/ObjectNotFoundException" )
    @Produces( { APPLICATION_XML, APPLICATION_JSON } )
    public Object throwObjectNotFoundException()
    {
        throw new ObjectNotFoundException( "ObjectNotFoundException" );
    }

    @GET
    @Path( "/BadRequestException" )
    @Produces( { APPLICATION_XML, APPLICATION_JSON } )
    public Object throwBadRequestException()
    {
        throw new BadRequestException( "BadRequestException" );
    }

}
