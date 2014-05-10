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
package org.sonatype.siesta.testsuite.proxyclient;

import java.util.List;

import javax.ws.rs.client.WebTarget;

import org.sonatype.siesta.testsuite.SiestaTestSupport;
import org.sonatype.siesta.testsuite.resources.Echo;
import org.sonatype.siesta.testsuite.resources.EchoResource;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test use of {@link Echo} proxy to access {@link EchoResource}.
 */
public class EchoTest
    extends SiestaTestSupport
{
  @Test
  public void basic() throws Exception {
    WebTarget target = client().target(url());
    Echo echo = ((ResteasyWebTarget)target).proxy(Echo.class);
    List<String> result = echo.get("hi");
    assertThat(result, notNullValue());
    assertThat(result, hasItem("foo=hi"));
  }
}
