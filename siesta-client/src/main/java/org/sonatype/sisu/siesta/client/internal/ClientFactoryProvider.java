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
package org.sonatype.sisu.siesta.client.internal;

import java.lang.reflect.Proxy;

import org.sonatype.sisu.siesta.client.ClientFactory;
import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;

/**
 * TODO
 *
 * @since 1.4
 */
public class ClientFactoryProvider<S>
    implements Provider<ClientFactory<S>>
{

    private final Class<S> serviceInterface;

    public ClientFactoryProvider( final Class<S> serviceInterface )
    {
        this.serviceInterface = serviceInterface;
    }

    //@Override
    public ClientFactory<S> get()
    {
        return new ClientFactory<S>()
        {
            public S create( final Client client, final String baseUrl )
            {
                final Class<?>[] interfaces = new Class<?>[]{ serviceInterface };
                return (S) Proxy.newProxyInstance(
                    serviceInterface.getClassLoader(),
                    interfaces,
                    new ClientInvocationHandler( serviceInterface, client, baseUrl ) );
            }
        };
    }

}