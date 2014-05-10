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

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.sonatype.siesta.Resource;
import org.sonatype.siesta.testsuite.model.UserXO;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

@Named
@Singleton
@Path("/user")
public class UserResource
    implements Resource
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  @GET
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public List<UserXO> get() {
    return Lists.newArrayList(
        new UserXO().withName("foo"),
        new UserXO().withName("bar")
    );
  }

  @GET
  @Path("/{id}")
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public UserXO get(@PathParam("id") String id) {
    if ("foo".equals(id)) {
      return new UserXO().withName("foo");
    }
    throw new NotFoundException("User with id '" + id + "' not found");
  }

  @PUT
  @Consumes({APPLICATION_XML, APPLICATION_JSON})
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public UserXO put(final UserXO user) {
    log.info("PUT user: {}", user);
    return user;
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") String id) {
    if (!"foo".equals(id)) {
      throw new NotFoundException("User with id '" + id + "' not found");
    }
  }
}
