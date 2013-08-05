/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
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

package org.sonatype.sisu.siesta.testsuite.support;

import org.sonatype.sisu.siesta.client.filters.ErrorsV1JsonFilter;
import org.sonatype.sisu.siesta.client.filters.ErrorsV1XmlFilter;
import org.sonatype.sisu.siesta.client.filters.ValidationErrorsV1JsonFilter;
import org.sonatype.sisu.siesta.client.filters.ValidationErrorsV1XmlFilter;

import com.sun.jersey.api.client.filter.LoggingFilter;
import org.junit.Before;

/**
 * Support class for Siesta Client UTs.
 *
 * @since 1.4
 */
public class SiestaClientTestSupport
    extends SiestaTestSupport
{

  @Before
  public void configureClient()
      throws Exception
  {
    client().addFilter(new LoggingFilter());
    client().addFilter(new ErrorsV1JsonFilter());
    client().addFilter(new ErrorsV1XmlFilter());
    client().addFilter(new ValidationErrorsV1JsonFilter());
    client().addFilter(new ValidationErrorsV1XmlFilter());
  }

}
