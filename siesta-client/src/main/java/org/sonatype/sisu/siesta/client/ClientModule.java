package org.sonatype.sisu.siesta.client;

import org.sonatype.sisu.siesta.client.internal.ClientFactoryProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.util.Types;

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