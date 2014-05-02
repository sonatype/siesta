package org.sonatype.siesta.jackson2;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson {@link ObjectMapper} provider for use with Siesta.
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
