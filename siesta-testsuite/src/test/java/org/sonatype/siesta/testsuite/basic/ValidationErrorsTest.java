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

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.sonatype.siesta.ValidationErrorXO;
import org.sonatype.siesta.testsuite.SiestaTestSupport;
import org.sonatype.siesta.testsuite.model.UserXO;

import org.junit.Test;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.sonatype.siesta.MediaTypes.VND_VALIDATION_ERRORS_V1_JSON_TYPE;
import static org.sonatype.siesta.MediaTypes.VND_VALIDATION_ERRORS_V1_XML_TYPE;

/**
 * Validation error response handling tests.
 */
public class ValidationErrorsTest
    extends SiestaTestSupport
{
  @Test
  public void put_multiple_manual_validations_XML() throws Exception {
    put_multiple_manual_validations(APPLICATION_XML_TYPE, VND_VALIDATION_ERRORS_V1_XML_TYPE);
  }

  @Test
  public void put_multiple_manual_validations_JSON() throws Exception {
    put_multiple_manual_validations(APPLICATION_JSON_TYPE, VND_VALIDATION_ERRORS_V1_JSON_TYPE);
  }

  private void put_multiple_manual_validations(final MediaType... mediaTypes) throws Exception {
    UserXO sent = new UserXO();

    Response response = client().target(url("validationErrors/manual/multiple")).request()
        .accept(mediaTypes)
        .put(Entity.entity(sent, mediaTypes[0]), Response.class);

    assertThat(response.getStatusInfo(), is(equalTo((StatusType)Status.BAD_REQUEST)));
    assertThat(response.getMediaType(), is(equalTo(mediaTypes[1])));

    List<ValidationErrorXO> errors = response.readEntity(new GenericType<List<ValidationErrorXO>>() {});
    assertThat(errors, hasSize(2));
  }

  @Test
  public void put_single_manual_validation_XML() throws Exception {
    put_single_manual_validation(APPLICATION_XML_TYPE, VND_VALIDATION_ERRORS_V1_XML_TYPE);
  }

  @Test
  public void put_single_manual_validation_JSON() throws Exception {
    put_single_manual_validation(APPLICATION_JSON_TYPE, VND_VALIDATION_ERRORS_V1_JSON_TYPE);
  }

  private void put_single_manual_validation(final MediaType... mediaTypes) throws Exception {
    UserXO sent = new UserXO();

    Response response = client().target(url("validationErrors/manual/single")).request()
        .accept(mediaTypes)
        .put(Entity.entity(sent, mediaTypes[0]), Response.class);

    assertThat(response.getStatusInfo(), is(equalTo((StatusType)Status.BAD_REQUEST)));
    assertThat(response.getMediaType(), is(equalTo(mediaTypes[1])));

    List<ValidationErrorXO> errors = response.readEntity(new GenericType<List<ValidationErrorXO>>() {});
    assertThat(errors, hasSize(1));
  }
}
