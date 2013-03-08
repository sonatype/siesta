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
package org.sonatype.sisu.siesta.client;

import java.lang.reflect.Proxy;

import org.sonatype.sisu.siesta.client.internal.ClientInvocationHandler;
import com.sun.jersey.api.client.Client;

/**
 * TODO
 *
 * @since 1.4
 */
public class ClientBuilder
{

    public static Target using( final Client client )
    {
        return new Target( client );
    }

    public static class Target
    {

        private final Client client;

        public Target( final Client client )
        {
            this.client = client;
        }

        public Factory toAccess( final String url )
        {
            return new Factory( url );
        }

        public class Factory
        {

            private final String url;

            public Factory( final String url )
            {
                this.url = url;
            }

            public <T> T build( final Class<T> clientType )
            {
                return (T) Proxy.newProxyInstance(
                    clientType.getClassLoader(),
                    new Class[]{ clientType },
                    new ClientInvocationHandler( clientType, client, url ) );
            }

        }

    }
}
