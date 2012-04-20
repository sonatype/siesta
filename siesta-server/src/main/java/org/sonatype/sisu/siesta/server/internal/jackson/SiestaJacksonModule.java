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
package org.sonatype.sisu.siesta.server.internal.jackson;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Singleton;

/**
 * Siesta Jackson Guice module.
 *
 * @since 1.0
 */
public class SiestaJacksonModule
    implements Module
{
    public void configure(final Binder binder) {
        binder.bind(JacksonProvider.class).in(Singleton.class);
        binder.bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).in(Singleton.class);
    }
}
