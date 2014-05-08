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
package org.sonatype.sisu.siesta.common.validation;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A failing validation.
 *
 * @since 1.4
 */
@XmlRootElement(name = "validationError")
public class ValidationErrorXO
{

  /**
   * Denotes that validation does not applies to a specific value.
   */
  public static final String GENERIC = "*";

  /**
   * Validation id. Identifies the value value that is failing validation. A value of "*" denotes that validation
   * does
   * not applies to a specific value.
   * E.g. "name".
   */
  private String id;

  /**
   * Description of failing validation.
   * E.g. "Name cannot be null".
   */
  private String message;

  public ValidationErrorXO() {
    id = GENERIC;
  }

  /**
   * Creates a validation error that does not applies to a specific value.
   *
   * @param message validation description
   */
  public ValidationErrorXO(final String message) {
    this(GENERIC, message);
  }

  /**
   * Creates a validation error for a specific value.
   *
   * @param id      identifier of value failing validation.
   * @param message validation description
   */
  public ValidationErrorXO(final String id, final String message) {
    this.id = id == null ? GENERIC : id;
    this.message = message;
  }

  /**
   * @return identifier of value failing validation (never null).  A value of "*" denotes that validation does
   *         not applies to a specific value.
   */
  public String getId() {
    return id;
  }

  /**
   * @param id of value failing validation
   */
  public void setId(final String id) {
    this.id = id == null ? GENERIC : id;
  }

  /**
   * @param id of value failing validation
   * @return itself, for fluent api usage
   */
  public ValidationErrorXO withId(final String id) {
    setId(id);
    return this;
  }

  /**
   * @return validation description
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message validation description
   */
  public void setMessage(final String message) {
    this.message = message;
  }

  /**
   * @param message validation description
   * @return itself, for fluent api usage
   */
  public ValidationErrorXO withMessage(final String message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "id='" + id + '\'' +
        ", message='" + message + '\'' +
        '}';
  }

}