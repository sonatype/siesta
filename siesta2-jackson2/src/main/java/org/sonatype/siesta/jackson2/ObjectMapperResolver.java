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
