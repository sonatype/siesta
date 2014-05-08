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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Variant;
import javax.ws.rs.ext.ExceptionMapper;

import static org.sonatype.siesta.MediaTypes.VND_ERROR_V1_JSON_TYPE;
import static org.sonatype.siesta.MediaTypes.VND_ERROR_V1_XML_TYPE;

/**
 * Support for {@link ExceptionMapper} implementations.
 *
 * @since 1.0
 */
public abstract class FaultExceptionMapperSupport<E extends Throwable>
    extends ExceptionMapperSupport<E>
{
  private final List<Variant> variants;

  public FaultExceptionMapperSupport() {
    this.variants = Variant.mediaTypes(
        VND_ERROR_V1_JSON_TYPE,
        VND_ERROR_V1_XML_TYPE
    ).add().build();
  }

  protected Response convert(final E exception, final String id) {
    Response.ResponseBuilder builder = Response.status(getStatus(exception));

    Variant variant = getRequest().selectVariant(variants);
    if (variant != null) {
      builder.type(variant.getMediaType()).entity(new FaultXO(id, getMessage(exception)));
    }

    return builder.build();
  }

  protected String getMessage(final E exception) {
    return exception.getMessage();
  }

  protected Status getStatus(final E exception) {
    return Status.INTERNAL_SERVER_ERROR;
  }

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
