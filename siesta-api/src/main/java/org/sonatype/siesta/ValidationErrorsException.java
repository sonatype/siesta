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
package org.sonatype.siesta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Thrown when there are request validation errors.
 *
 * @since 2.0
 * @see ValidationErrorXO
 */
public class ValidationErrorsException
    extends RuntimeException
{

  private final List<ValidationErrorXO> errors = new ArrayList<ValidationErrorXO>();

  public ValidationErrorsException() {
    super();
  }

  public ValidationErrorsException(final String message) {
    errors.add(new ValidationErrorXO(message));
  }

  public ValidationErrorsException(final String id, final String message) {
    errors.add(new ValidationErrorXO(id, message));
  }

  public ValidationErrorsException withError(final String message) {
    errors.add(new ValidationErrorXO(message));
    return this;
  }

  public ValidationErrorsException withError(final String id, final String message) {
    errors.add(new ValidationErrorXO(id, message));
    return this;
  }

  public ValidationErrorsException withErrors(final ValidationErrorXO... validationErrors) {
    if (validationErrors == null) {
      throw new NullPointerException();
    }
    errors.addAll(Arrays.asList(validationErrors));
    return this;
  }

  public ValidationErrorsException withErrors(final List<ValidationErrorXO> validationErrors) {
    if (validationErrors == null) {
      throw new NullPointerException();
    }
    errors.addAll(validationErrors);
    return this;
  }

  public List<ValidationErrorXO> getValidationErrors() {
    return errors;
  }

  public boolean hasValidationErrors() {
    return !errors.isEmpty();
  }

  @Override
  public String getMessage() {
    final StringBuilder sb = new StringBuilder();
    for (final ValidationErrorXO error : errors) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(error.getMessage());
    }
    return sb.length() == 0 ? "(No validation errors)" : sb.toString();
  }
}
