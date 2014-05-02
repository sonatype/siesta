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
package org.sonatype.sisu.siesta.common.error;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A description of an exception / error.
 *
 * @since 1.4
 */
@XmlRootElement(name = "error")
public class ErrorXO
{

  /**
   * A unique id (usually generated), for tracing purposes.
   */
  private String id;

  /**
   * Error description.
   */
  private String message;

  public ErrorXO() {
    super();
  }

  /**
   * @param id      a unique id (usually generated), for tracing purposes
   * @param message error description
   */
  public ErrorXO(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  /**
   * @return id error (unique) identifier
   */
  public String getId() {
    return id;
  }

  /**
   * @param id a unique id (usually generated), for tracing purposes
   */
  public void setId(final String id) {
    this.id = id;
  }

  /**
   * @param id a unique id (usually generated), for tracing purposes
   * @return itself, for fluent api usage
   */
  public ErrorXO withId(final String id) {
    this.id = id;
    return this;
  }

  /**
   * @return error description
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message error description
   */
  public void setMessage(final String message) {
    this.message = message;
  }

  /**
   * @param message error description
   * @return itself, for fluent api usage
   */
  public ErrorXO withMessage(final String message) {
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