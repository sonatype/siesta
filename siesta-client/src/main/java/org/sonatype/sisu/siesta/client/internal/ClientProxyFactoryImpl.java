/*
 * Copyright (c) 2007-2011 Sonatype, Inc. All rights reserved.
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
package org.sonatype.sisu.siesta.client.internal;

import org.sonatype.sisu.siesta.client.ClientProxyFactory;
import org.sonatype.restsimple.client.WebProxy;

import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link ClientProxyFactory} implementation.
 *
 * @since 1.0
 */
public class ClientProxyFactoryImpl
    implements ClientProxyFactory
{
    public <T> T create(final Class<T> type, final URI uri) {
        checkNotNull(type);
        return WebProxy.createProxy(type, uri);
    }
}