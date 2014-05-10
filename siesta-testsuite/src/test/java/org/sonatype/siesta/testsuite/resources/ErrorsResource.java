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
package org.sonatype.siesta.testsuite.resources;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import org.sonatype.siesta.Resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

@Named
@Singleton
@Path("/errors")
public class ErrorsResource
    implements Resource
{
  @GET
  @Path("/NotFoundException")
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public Object throwObjectNotFoundException() {
    throw new NotFoundException("NotFoundException");
  }

  @GET
  @Path("/BadRequestException")
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public Object throwBadRequestException() {
    throw new BadRequestException("BadRequestException");
  }

  @GET
  @Path("/406")
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public Object throw406() {
    throw new WebApplicationException(406);
  }
}
