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
package org.sonatype.siesta.server.internal.resteasy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.ValidationErrorXO;
import org.sonatype.siesta.server.validation.ValidationExceptionMapperSupport;

import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;

import static org.jboss.resteasy.api.validation.ConstraintType.Type.RETURN_VALUE;

/**
 * Maps {@link ResteasyViolationException} to {@link Status#BAD_REQUEST} or {@link Status#INTERNAL_SERVER_ERROR}
 * in case of a violation on a methods return value, with a list of {@link ValidationErrorXO} as body.
 *
 * @since 2.0
 */
@Named
@Singleton
@Provider
public class ResteasyViolationExceptionMapper
    extends ValidationExceptionMapperSupport<ResteasyViolationException>
{
  @Override
  protected List<ValidationErrorXO> getValidationErrors(final ResteasyViolationException exception) {
    return getValidationErrors(exception.getViolations());
  }

  @Override
  protected Status getStatus(final ResteasyViolationException exception) {
    return getResponseStatus(exception.getViolations());
  }

  private List<ValidationErrorXO> getValidationErrors(final List<ResteasyConstraintViolation> violations) {
    final List<ValidationErrorXO> errors = new ArrayList<>();

    for (final ResteasyConstraintViolation violation : violations) {
      errors.add(new ValidationErrorXO(getPath(violation), violation.getMessage()));
    }

    return errors;
  }

  private Status getResponseStatus(final List<ResteasyConstraintViolation> violations) {
    final Iterator<ResteasyConstraintViolation> iterator = violations.iterator();

    if (iterator.hasNext()) {
      return getResponseStatus(iterator.next());
    }
    else {
      return Status.BAD_REQUEST;
    }
  }

  private Status getResponseStatus(final ResteasyConstraintViolation violation) {
    if (RETURN_VALUE.equals(violation.getConstraintType())) {
      return Status.INTERNAL_SERVER_ERROR;
    }
    return Status.BAD_REQUEST;
  }

  private String getPath(final ResteasyConstraintViolation violation) {
    final String propertyPath = violation.getPath().toString();

    return violation.type() + (!"".equals(propertyPath) ? ' ' + propertyPath : "");
  }
}
