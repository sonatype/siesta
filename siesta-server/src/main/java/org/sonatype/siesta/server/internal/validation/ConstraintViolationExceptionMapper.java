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
package org.sonatype.siesta.server.internal.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.ws.rs.core.Response.Status;

import org.sonatype.siesta.ValidationErrorXO;

/**
 * Maps {@link ConstraintViolationException} to {@link Status#BAD_REQUEST} (or {@link Status#INTERNAL_SERVER_ERROR}
 * in case of a violation on a methods return value) with a list of {@link ValidationErrorXO} as body.
 *
 * @since 2.0
 */
@Named
@Singleton
public class ConstraintViolationExceptionMapper
    extends ValidationErrorsExceptionMappersSupport<ConstraintViolationException>
{
  @Override
  protected List<ValidationErrorXO> getValidationErrors(final ConstraintViolationException exception) {
    return getValidationErrors(exception.getConstraintViolations());
  }

  @Override
  protected int getStatusCode(final ConstraintViolationException exception) {
    return getResponseStatus(exception.getConstraintViolations()).getStatusCode();
  }

  private List<ValidationErrorXO> getValidationErrors(final Set<ConstraintViolation<?>> violations) {
    final List<ValidationErrorXO> errors = new ArrayList<>();

    for (final ConstraintViolation violation : violations) {
      errors.add(new ValidationErrorXO(getPath(violation), violation.getMessage()));
    }

    return errors;
  }

  private Status getResponseStatus(final Set<ConstraintViolation<?>> constraintViolations) {
    final Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();

    if (iterator.hasNext()) {
      return getResponseStatus(iterator.next());
    }
    else {
      return Status.BAD_REQUEST;
    }
  }

  private Status getResponseStatus(final ConstraintViolation<?> constraintViolation) {
    for (Path.Node node : constraintViolation.getPropertyPath()) {
      ElementKind kind = node.getKind();

      if (ElementKind.RETURN_VALUE.equals(kind)) {
        return Status.INTERNAL_SERVER_ERROR;
      }
    }

    return Status.BAD_REQUEST;
  }

  private String getPath(final ConstraintViolation violation) {
    final String leafBeanName = violation.getLeafBean().getClass().getSimpleName();
    final String propertyPath = violation.getPropertyPath().toString();

    return leafBeanName + (!"".equals(propertyPath) ? '.' + propertyPath : "");
  }
}