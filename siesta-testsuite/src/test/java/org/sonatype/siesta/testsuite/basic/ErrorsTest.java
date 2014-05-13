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
package org.sonatype.siesta.testsuite.basic;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.sonatype.siesta.ExceptionMapperSupport;
import org.sonatype.siesta.testsuite.SiestaTestSupport;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests error handling.
 */
public class ErrorsTest
    extends SiestaTestSupport
{
  @Test
  public void errorResponseHasFaultId() throws Exception {
    WebTarget target = client().target(url("errors/406"));
    Response response = target.request().get(Response.class);
    log("Status: {}", response.getStatusInfo());

    assertThat(response.getStatusInfo().getStatusCode(), equalTo(406));
    String faultId = response.getHeaderString(ExceptionMapperSupport.X_SIESTA_FAULT_ID);
    log("Fault ID: {}", faultId);
    assertThat(faultId, notNullValue());
  }
}
