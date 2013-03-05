/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
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
