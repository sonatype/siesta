package org.sonatype.sisu.siesta.client.internal;

import java.lang.reflect.Proxy;
import java.net.URI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.sisu.siesta.client.ClientFactory;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.spi.Toolable;
import com.sun.jersey.api.client.Client;

@Named
@Singleton
public class ClientFactoryProvider<S>
    implements Provider<ClientFactory<S>>
{

    private final Class<S> serviceInterface;

    private Injector injector;

    public ClientFactoryProvider( final Class<S> serviceInterface )
    {
        this.serviceInterface = serviceInterface;
    }

    //@Override
    public ClientFactory<S> get()
    {
//    final DefaultRestClientFactory<S> restClientFactory = new DefaultRestClientFactory<S>(serviceInterface, bindings);
//    injector.injectMembers(restClientFactory);
//    return restClientFactory;
        return new ClientFactory<S>()
        {
            public S create( final Client client, final String baseUrl )
            {
                final Class<?>[] interfaces = new Class<?>[]{ serviceInterface };
                return (S) Proxy.newProxyInstance(
                    serviceInterface.getClassLoader(),
                    interfaces,
                    new ClientImpl( serviceInterface, client, baseUrl ) );
            }
        };
    }

    @Inject
    @Toolable
    void initialize( final Injector injector )
    {
        this.injector = injector;
    }

}