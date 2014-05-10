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
package org.sonatype.sisu.siesta.testsuite;

import java.util.Date;
import java.util.UUID;

import org.sonatype.sisu.siesta.client.ClientBuilder;
import org.sonatype.sisu.siesta.testsuite.clients.Users;
import org.sonatype.sisu.siesta.testsuite.model.UserXO;
import org.sonatype.sisu.siesta.testsuite.support.SiestaTestSupport;

import com.sun.jersey.api.client.ClientResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @since 1.5.1
 */
public class ClientWithoutFiltersTest
    extends SiestaTestSupport
{

  private Users users;

  @Before
  public void createClient() {
    users = ClientBuilder.using(client()).toAccess(url()).build(Users.class);
  }

  @Test
  public void inexistentPathReturnsClientResponse()
      throws Exception
  {
    final ClientResponse response = users.inexistentReturnsClientResponse();

    assertThat(response.getClientResponseStatus(), Matchers.is(ClientResponse.Status.NOT_FOUND));
  }

  @Test
  public void putReturnsClientResponse()
      throws Exception
  {
    final UserXO sent = new UserXO().withName(UUID.randomUUID().toString()).withCreated(new Date());
    final ClientResponse response = users.putReturnsClientResponse(sent);

    assertThat(response.getClientResponseStatus(), Matchers.is(ClientResponse.Status.OK));

    final UserXO received = response.getEntity(UserXO.class);

    assertThat(received, is(notNullValue()));
    assertThat(received.getName(), is(equalTo(sent.getName())));
    assertThat(received.getCreated(), is(equalTo(sent.getCreated())));
  }

}
