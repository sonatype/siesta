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
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.sonatype.siesta.Resource;
import org.sonatype.siesta.ValidationErrorsException;
import org.sonatype.siesta.testsuite.model.UserXO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * @since 1.4
 */
@Named
@Singleton
@Path("/validationErrors")
public class ValidationErrorsResource
    implements Resource
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  @PUT
  @Path("/manual/multiple")
  @Consumes({APPLICATION_XML, APPLICATION_JSON})
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public UserXO putWithMultipleManualValidations(final UserXO user) {
    log.info("PUT user: {}", user);

    final ValidationErrorsException validationErrors = new ValidationErrorsException();
    if (user.getName() == null) {
      validationErrors.withError("name", "Name cannot be null");
    }
    if (user.getDescription() == null) {
      validationErrors.withError("description", "Description cannot be null");
    }

    if (validationErrors.hasValidationErrors()) {
      throw validationErrors;
    }

    return user;
  }

  @PUT
  @Path("/manual/single")
  @Consumes({APPLICATION_XML, APPLICATION_JSON})
  @Produces({APPLICATION_XML, APPLICATION_JSON})
  public UserXO putWithSingleManualValidation(final UserXO user) {
    log.info("PUT user: {}", user);

    if (user.getName() == null) {
      throw new ValidationErrorsException("name", "Name cannot be null");
    }
    if (user.getDescription() == null) {
      throw new ValidationErrorsException("description", "Description cannot be null");
    }

    return user;
  }
}
