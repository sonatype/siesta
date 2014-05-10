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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Variant;

import org.sonatype.siesta.ExceptionMapperSupport;
import org.sonatype.siesta.ValidationErrorXO;

import static org.sonatype.siesta.MediaTypes.VND_VALIDATION_ERRORS_V1_JSON_TYPE;
import static org.sonatype.siesta.MediaTypes.VND_VALIDATION_ERRORS_V1_XML_TYPE;

/**
 * Support calls for exception mappers returning {@link Status#BAD_REQUEST} with {@link ValidationErrorXO}s in body.
 *
 * @since 2.0
 */
public abstract class ValidationExceptionMapperSupport<E extends Throwable>
    extends ExceptionMapperSupport<E>
{
  private final List<Variant> variants;

  public ValidationExceptionMapperSupport() {
    variants = Variant.mediaTypes(
        VND_VALIDATION_ERRORS_V1_JSON_TYPE, VND_VALIDATION_ERRORS_V1_XML_TYPE
    ).add().build();
  }

  @Override
  protected Response convert(final E exception, final String id) {
    final Response.ResponseBuilder builder = Response.status(getStatusCode(exception));

    final List<ValidationErrorXO> validationErrors = getValidationErrors(exception);
    if (validationErrors != null && !validationErrors.isEmpty()) {
      final Variant variant = getRequest().selectVariant(variants);
      if (variant != null) {
        builder.type(variant.getMediaType())
            .entity(
                new GenericEntity<List<ValidationErrorXO>>(validationErrors)
                {
                  @Override
                  public String toString() {
                    return getEntity().toString();
                  }
                }
            );
      }
    }

    return builder.build();
  }

  protected Status getStatusCode(final E exception) {
    return Status.BAD_REQUEST;
  }

  protected abstract List<ValidationErrorXO> getValidationErrors(final E exception);

  //
  // Helpers
  //

  private Provider<Request> requestProvider;

  @Inject
  public void setRequestProvider(final Provider<Request> requestProvider) {
    if (requestProvider == null) {
      throw new NullPointerException();
    }
    this.requestProvider = requestProvider;
  }

  protected Request getRequest() {
    if (requestProvider == null) {
      throw new IllegalStateException();
    }
    return requestProvider.get();
  }
}