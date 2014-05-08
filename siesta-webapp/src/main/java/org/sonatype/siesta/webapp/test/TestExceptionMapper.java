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
package org.sonatype.siesta.webapp.test;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.ExceptionMapperSupport;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * Test {@link ExceptionMapper}.
 *
 * @since 2.0
 */
@Named
@Singleton
@Provider
public class TestExceptionMapper
  extends ExceptionMapperSupport<TestException>
{
  @Inject
  public TestExceptionMapper() {
    if (log.isTraceEnabled()) {
      log.trace("Created", new Throwable("MARKER"));
    }
    else {
      log.debug("Created");
    }
  }

  @Override
  protected Response convert(final TestException exception, final String id) {
    return Response
        .serverError()
        .entity("Error: " + exception)
        .type(TEXT_PLAIN)
        .build();
  }
}
