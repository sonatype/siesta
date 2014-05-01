package org.sonatype.siesta.jackson;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.sonatype.siesta.Component;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 * ???
 *
 * @since 2.0
 */
//@Named
//@Singleton
@Provider
public class ObjectMapperProvider
  implements ContextResolver<ObjectMapper>, Component
{
  @Override
  public ObjectMapper getContext(final Class<?> type) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(Feature.INDENT_OUTPUT, true);
    return mapper;
  }
}
