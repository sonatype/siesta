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
package org.sonatype.siesta.server.internal.resteasy;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ParamConverterProvider;

import org.jboss.resteasy.client.exception.mapper.ClientExceptionMapper;
import org.jboss.resteasy.core.MediaTypeMap;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.StringConverter;
import org.jboss.resteasy.spi.StringParameterUnmarshaller;
import org.jboss.resteasy.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sisu {@link ResteasyProviderFactory} that supports removal of providers.
 *
 * @since 2.2
 */
public class SisuResteasyProviderFactory
    extends ResteasyProviderFactory
{
  private static final Logger log = LoggerFactory.getLogger(SisuResteasyProviderFactory.class);

  /**
   * Unregisters a @Provider type from this factory.
   *
   * @param provider type
   */
  public void removeRegistrations(Class<?> type) {
    log.debug("Removing registrations for: {}", type.getName());

    classContracts.remove(type);

    removeInstancesOf(type, providerInstances);
    providerClasses.remove(type);

    if (ExceptionMapper.class.isAssignableFrom(type)) {
      removeInstancesOf(type, exceptionMappers.values());
    }
    else if (MessageBodyReader.class.isAssignableFrom(type)) {
      clearInstancesOf(type, clientMessageBodyReaders, DUMMY_READER);
      clearInstancesOf(type, serverMessageBodyReaders, DUMMY_READER);
    }
    else if (MessageBodyWriter.class.isAssignableFrom(type)) {
      clearInstancesOf(type, clientMessageBodyWriters, DUMMY_WRITER);
      clearInstancesOf(type, serverMessageBodyWriters, DUMMY_WRITER);
    }
    else if (ContextResolver.class.isAssignableFrom(type)) {
      Type[] args = Types.getActualTypeArgumentsOfAnInterface(type, ContextResolver.class);
      contextResolvers.remove(Types.getRawType(args[0]));
    }
    else if (Feature.class.isAssignableFrom(type)) {
      removeInstancesOf(type, featureInstances);
      removeInstancesOf(type, enabledFeatures);
      featureClasses.remove(type);
    }
    else if (DynamicFeature.class.isAssignableFrom(type)) {
      removeInstancesOf(type, clientDynamicFeatures);
      removeInstancesOf(type, serverDynamicFeatures);
    }
    else if (ParamConverterProvider.class.isAssignableFrom(type)) {
      removeInstancesOf(type, paramConverterProviders);
    }
    else if (ClientExceptionMapper.class.isAssignableFrom(type)) {
      removeInstancesOf(type, clientExceptionMappers.values());
    }
    else if (StringConverter.class.isAssignableFrom(type)) {
      removeInstancesOf(type, stringConverters.values());
    }
    else if (StringParameterUnmarshaller.class.isAssignableFrom(type)) {
      stringParameterUnmarshallers.values().remove(type);
    }
    else {
      log.warn("Unable to remove registrations for: {}", type.getName());
    }
  }

  /**
   * Remove any instances of the given type from the collection.
   */
  private static void removeInstancesOf(Class<?> type, Collection<?> collection) {
    for (Object o : collection) {
      if (type.isInstance(o)) {
        collection.remove(o); // ResteasyProviderFactory's collections are all concurrent, so this is safe
      }
    }
  }

  /**
   * Clear any instances of the given type from the {@link MediaTypeMap} by replacing them with the placeholder.
   *
   * Unfortunately removing the entries is tricky as they're scattered across multiple read-only nested maps.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static <T> void clearInstancesOf(Class type, MediaTypeMap<SortedKey<T>> mediaTypeMap, T placeholder) {
    for (SortedKey key : mediaTypeMap.getPossible(MediaType.WILDCARD_TYPE)) {
      if (type.isInstance(key.obj)) {
        key.readerClass = Void.class;
        key.template = Void.class;
        key.obj = placeholder;
      }
    }
    mediaTypeMap.getClassCache().clear();
  }

  /**
   * Dummy {@link MessageBodyReader} that never matches anything.
   */
  private static final MessageBodyReader<?> DUMMY_READER = new MessageBodyReader<Object>()
  {
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return false;
    }

    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
    {
      return null;
    }
  };

  /**
   * Dummy {@link MessageBodyWriter} that never matches anything.
   */
  private static final MessageBodyWriter<?> DUMMY_WRITER = new MessageBodyWriter<Object>()
  {
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return false;
    }

    public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
    {
      // no-op
    }

    public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
      return 0;
    }
  };
}
