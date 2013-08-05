/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Echo Resource client.
 *
 * @since 1.4.2
 */
@Path("/echo")
public interface Echo
{

  @GET
  List<String> get(@QueryParam("foo") String foo);

  @GET
  List<String> get(@QueryParam("foo") String foo, @QueryParam("bar") int bar);

  @GET
  List<String> get(@QueryParam("bar") int bar);

  @GET
  @Path("/multiple")
  List<String> get(@QueryParam("foo") String[] foo);

  @GET
  @Path("/multiple")
  List<String> get(@QueryParam("foo") Object[] foo);

  @GET
  @Path("/multiple")
  List<String> get(@QueryParam("foo") Collection<?> foo);

  @GET
  @Path("/multiple")
  List<String> get(@QueryParam("foo") Iterator foo);

  @GET
  List<String> get(@QueryParam("params") MultivaluedMap<String, String> queryParams);
}
