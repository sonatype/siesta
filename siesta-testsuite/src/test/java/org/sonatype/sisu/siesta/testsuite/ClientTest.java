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

package org.sonatype.sisu.siesta.testsuite;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import org.sonatype.sisu.siesta.client.ClientBuilder;
import org.sonatype.sisu.siesta.testsuite.clients.Echo;
import org.sonatype.sisu.siesta.testsuite.clients.Errors;
import org.sonatype.sisu.siesta.testsuite.clients.Users;
import org.sonatype.sisu.siesta.testsuite.model.UserXO;
import org.sonatype.sisu.siesta.testsuite.support.SiestaClientTestSupport;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.sonatype.sisu.siesta.client.ClientBuilder.Target.Factory;

/**
 * @since 1.4
 */
public class ClientTest
    extends SiestaClientTestSupport
{

  private Users users;

  private Errors errors;

  private Echo echo;

  @Before
  public void createClient() {
    final Factory factory = ClientBuilder.using(client()).toAccess(url());

    users = factory.build(Users.class);
    errors = factory.build(Errors.class);
    echo = factory.build(Echo.class);
  }

  @Test
  public void getJson()
      throws Exception
  {
    users.getJson();
  }

  @Test
  public void getXML()
      throws Exception
  {
    users.getXml();
  }

  @Test
  public void getByIdJson()
      throws Exception
  {
    users.getJson("foo");
  }

  @Test
  public void getByIdXml()
      throws Exception
  {
    users.getXml("foo");
  }

  @Test
  public void getByIdInexistentJson()
      throws Exception
  {
    thrown.expect(UniformInterfaceException.class);
    thrown.expectMessage("User with id 'test' not found");

    users.getJson("test");
  }

  @Test
  public void getByIdInexistentXml()
      throws Exception
  {
    thrown.expect(UniformInterfaceException.class);
    thrown.expectMessage("User with id 'test' not found");

    users.getXml("test");
  }

  @Test
  public void inexistentPath()
      throws Exception
  {
    thrown.expect(UniformInterfaceException.class);
    thrown.expect(new HttpStatusMatcher(404));

    users.inexistent();
  }

  @Test
  public void putJsonJson()
      throws Exception
  {
    final UserXO sent = new UserXO().withName(UUID.randomUUID().toString()).withCreated(new Date());
    final UserXO received = users.putJsonJson(sent);

    assertThat(received, is(notNullValue()));
    assertThat(received.getName(), is(equalTo(sent.getName())));
    assertThat(received.getCreated(), is(equalTo(sent.getCreated())));
  }

  @Test
  public void putXmlXml()
      throws Exception
  {
    final UserXO sent = new UserXO().withName(UUID.randomUUID().toString()).withCreated(new Date());
    final UserXO received = users.putXmlXml(sent);

    assertThat(received, is(notNullValue()));
    assertThat(received.getName(), is(equalTo(sent.getName())));
    assertThat(received.getCreated(), is(equalTo(sent.getCreated())));
  }

  @Test
  public void putXmlJson()
      throws Exception
  {
    final UserXO sent = new UserXO().withName(UUID.randomUUID().toString()).withCreated(new Date());
    final UserXO received = users.putXmlJson(sent);

    assertThat(received, is(notNullValue()));
    assertThat(received.getName(), is(equalTo(sent.getName())));
    assertThat(received.getCreated(), is(equalTo(sent.getCreated())));
  }

  @Test
  public void putJsonXml()
      throws Exception
  {
    final UserXO sent = new UserXO().withName(UUID.randomUUID().toString()).withCreated(new Date());
    final UserXO received = users.putJsonXml(sent);

    assertThat(received, is(notNullValue()));
    assertThat(received.getName(), is(equalTo(sent.getName())));
    assertThat(received.getCreated(), is(equalTo(sent.getCreated())));
  }

  @Test
  public void put()
      throws Exception
  {
    final UserXO sent = new UserXO().withName(UUID.randomUUID().toString()).withCreated(new Date());
    final UserXO received = users.put(sent);

    assertThat(received, is(notNullValue()));
    assertThat(received.getName(), is(equalTo(sent.getName())));
    assertThat(received.getCreated(), is(equalTo(sent.getCreated())));
  }

  @Test
  public void throwObjectNotFoundException()
      throws Exception
  {
    thrown.expect(UniformInterfaceException.class);
    thrown.expect(new HttpStatusMatcher(404));
    thrown.expectMessage("ObjectNotFoundException");

    errors.throwObjectNotFoundException();
  }

  @Test
  public void throwBadRequestException()
      throws Exception
  {
    thrown.expect(UniformInterfaceException.class);
    thrown.expect(new HttpStatusMatcher(400));
    thrown.expectMessage("BadRequestException");

    errors.throwBadRequestException();
  }

  @Test
  public void queryParamString()
      throws Exception
  {
    final List<String> received = echo.get("bar");

    assertThat(received, is(notNullValue()));
    assertThat(received, contains("foo=bar"));
  }

  @Test
  public void queryParamInt()
      throws Exception
  {
    final List<String> received = echo.get(142);

    assertThat(received, is(notNullValue()));
    assertThat(received, contains("bar=142"));
  }

  @Test
  public void queryParamStringAndInt()
      throws Exception
  {
    final List<String> received = echo.get("v1", 142);

    assertThat(received, is(notNullValue()));
    assertThat(received, containsInAnyOrder("foo=v1", "bar=142"));
  }

  @Test
  public void queryParamStringArray()
      throws Exception
  {
    final List<String> received = echo.get(new String[]{"v1", "v2"});

    assertThat(received, is(notNullValue()));
    assertThat(received, containsInAnyOrder("foo=v1", "foo=v2"));
  }

  @Test
  public void queryParamObjectArray()
      throws Exception
  {
    final List<String> received = echo.get(new Object[]{"v1", 142});

    assertThat(received, is(notNullValue()));
    assertThat(received, containsInAnyOrder("foo=v1", "foo=142"));
  }

  @Test
  public void queryParamStringList()
      throws Exception
  {
    final List<String> values = Lists.newArrayList("v1", "v2");
    final List<String> received = echo.get(values);

    assertThat(received, is(notNullValue()));
    assertThat(received, containsInAnyOrder("foo=v1", "foo=v2"));
  }

  @Test
  public void queryParamObjectSet()
      throws Exception
  {
    final Set<Object> values = Sets.<Object>newHashSet("v1", 142);
    final List<String> received = echo.get(values);

    assertThat(received, is(notNullValue()));
    assertThat(received, containsInAnyOrder("foo=v1", "foo=142"));
  }

  @Test
  public void queryParamIterator()
      throws Exception
  {
    final List<String> values = Lists.newArrayList("v1", "v2");
    final List<String> received = echo.get(values.iterator());

    assertThat(received, is(notNullValue()));
    assertThat(received, containsInAnyOrder("foo=v1", "foo=v2"));
  }

  @Test
  public void queryParamMultiValuedMap()
      throws Exception
  {
    final MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    queryParams.putSingle("foo", "v1");
    queryParams.putSingle("bar", "142");

    final List<String> received = echo.get(queryParams);

    assertThat(received, is(notNullValue()));
    assertThat(received, containsInAnyOrder("foo=v1", "bar=142"));
  }

  private static class HttpStatusMatcher
      extends TypeSafeMatcher<UniformInterfaceException>
  {

    private final int expectedStatus;

    HttpStatusMatcher(final int expectedStatus) {
      this.expectedStatus = expectedStatus;
    }

    @Override
    protected boolean matchesSafely(final UniformInterfaceException e) {
      return e.getResponse().getStatus() == expectedStatus;
    }

    public void describeTo(final Description description) {
      description.appendText("HTTP Status " + expectedStatus);
    }
  }

}
