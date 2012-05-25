/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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

package org.sonatype.sisu.siesta.xstream;

import com.google.common.collect.Sets;
import com.sun.jersey.core.util.ReaderWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <a href="http://xstream.codehaus.org/">XStream</a> XML provider.
 *
 * @since 1.2
 */
@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.WILDCARD})
@Consumes({MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.WILDCARD})
@Provider
public class XStreamXmlProvider
    implements MessageBodyReader<Object>, MessageBodyWriter<Object>
{
    private static final Logger log = LoggerFactory.getLogger(XStreamXmlProvider.class);

    private final XStream xstream;

    private final Set<Class<?>> processed = Sets.newHashSet();

    public XStreamXmlProvider(final XStream xstream) {
        this.xstream = checkNotNull(xstream);
    }

    public XStreamXmlProvider() {
        this(new XStream()
        {
            {
                this.autodetectAnnotations(true);
            }
        });
    }

    protected XStream getXStream(final Class<?> type) {
        // TODO: Sort this out, this might not be safe, or necessary if autodetectAnnotations(true) ?
        synchronized (processed) {
            if (!processed.contains(type)) {
                log.trace("Processing annotations: {}", type);
                xstream.processAnnotations(type);
                processed.add(type);
            }
        }
        return xstream;
    }

    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public Object readFrom(final Class<Object> type,
                           final Type genericType,
                           final Annotation[] annotations,
                           final MediaType mediaType,
                           final MultivaluedMap<String, String> httpHeaders,
                           final InputStream entityStream)
        throws IOException, WebApplicationException
    {
        Charset cs = ReaderWriter.getCharset(mediaType);
        XStream xs = getXStream(type);
        return xs.fromXML(new InputStreamReader(entityStream, cs));
    }

    public void writeTo(final Object o,
                        final Class<?> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream entityStream)
        throws IOException, WebApplicationException
    {
        Charset cs = ReaderWriter.getCharset(mediaType);
        XStream xs = getXStream(type);
        xs.marshal(o, new CompactWriter(new OutputStreamWriter(entityStream, cs)));
    }
}