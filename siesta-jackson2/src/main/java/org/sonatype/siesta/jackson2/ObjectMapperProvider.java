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
package org.sonatype.siesta.jackson2;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson {@link ObjectMapper} (guice) provider for use with Siesta.
 *
 * @since 2.0
 */
@Named("siesta")
@Singleton
public class ObjectMapperProvider
  implements Provider<ObjectMapper>
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  private ObjectMapper mapper;

  private ObjectMapper create() {
    ObjectMapper mapper = new ObjectMapper();

    // Pretty print output
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    // Write dates as ISO-8601
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return mapper;
  }

  @Override
  public ObjectMapper get() {
    if (mapper == null) {
      mapper = create();
      log.debug("Mapper: {}", mapper);
    }
    return mapper;
  }
}
