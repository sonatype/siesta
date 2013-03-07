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

import org.sonatype.sisu.siesta.client.internal.ClientFactoryProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.util.Types;

/**
 * TODO
 *
 * @since 1.4
 */
public class ClientModule
{

    private ClientModule()
    {
    }

    public static <S> Module clientModuleFor( final Class<S> serviceInterface )
    {
        return new AbstractModule()
        {
            @Override
            protected void configure()
            {
                final Provider<ClientFactory<S>> provider = new ClientFactoryProvider<S>( serviceInterface );
                bind( (Key) Key.get( Types.newParameterizedType( ClientFactory.class, serviceInterface ) ) )
                    .toProvider( provider );
            }
        };
    }

}