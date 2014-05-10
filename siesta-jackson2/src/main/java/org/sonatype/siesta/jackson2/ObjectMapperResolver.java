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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson {@link ObjectMapper} resolver.
 *
 * This will use the mapper bound to the name "siesta".
 *
 * @since 2.0
 * @see ObjectMapperProvider
 */
@Named
@Singleton
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperResolver
  implements ContextResolver<ObjectMapper>, Component
{
  private final javax.inject.Provider<ObjectMapper> mapperProvider;

  @Inject
  public ObjectMapperResolver(final @Named("siesta") javax.inject.Provider<ObjectMapper> mapperProvider) {
    this.mapperProvider = mapperProvider;
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return mapperProvider.get();
  }
}
