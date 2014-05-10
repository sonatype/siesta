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
package org.sonatype.sisu.siesta.testsuite.clients;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.sonatype.sisu.siesta.client.Filters;
import org.sonatype.sisu.siesta.client.filters.ErrorsV1JsonFilter;
import org.sonatype.sisu.siesta.client.filters.ErrorsV1XmlFilter;
import org.sonatype.sisu.siesta.client.filters.ValidationErrorsV1JsonFilter;
import org.sonatype.sisu.siesta.client.filters.ValidationErrorsV1XmlFilter;
import org.sonatype.sisu.siesta.testsuite.model.UserXO;

import com.sun.jersey.api.client.ClientResponse;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * TODO
 *
 * @since 1.0
 */
@Path("/user")
@Filters({
    ErrorsV1JsonFilter.class,
    ErrorsV1XmlFilter.class,
    ValidationErrorsV1JsonFilter.class,
    ValidationErrorsV1XmlFilter.class
})
public interface Users
{

  @GET
  @Path("/inexistent/path")
  void inexistent();

  @GET
  @Path("/inexistent/path")
  ClientResponse inexistentReturnsClientResponse();

  @GET
  @Consumes({APPLICATION_JSON})
  List<UserXO> getJson();

  @GET
  @Consumes({APPLICATION_XML})
  List<UserXO> getXml();

  @GET
  @Path("/{id}")
  @Consumes({APPLICATION_JSON})
  UserXO getJson(@PathParam("id") String id);

  @GET
  @Path("/{id}")
  @Consumes({APPLICATION_XML})
  UserXO getXml(@PathParam("id") String id);

  @PUT
  @Produces({APPLICATION_JSON})
  @Consumes({APPLICATION_JSON})
  UserXO putJsonJson(final UserXO user);

  @PUT
  @Produces({APPLICATION_XML})
  @Consumes({APPLICATION_XML})
  UserXO putXmlXml(final UserXO user);

  @PUT
  @Produces({APPLICATION_XML})
  @Consumes({APPLICATION_JSON})
  UserXO putXmlJson(final UserXO user);

  @PUT
  @Produces({APPLICATION_JSON})
  @Consumes({APPLICATION_XML})
  UserXO putJsonXml(final UserXO user);

  @PUT
  UserXO put(final UserXO user);

  @PUT
  ClientResponse putReturnsClientResponse(final UserXO user);

  @DELETE
  @Path("/{id}")
  void delete(@PathParam("id") String id);

}
