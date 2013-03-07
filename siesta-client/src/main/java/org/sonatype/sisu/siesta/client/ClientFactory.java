package org.sonatype.sisu.siesta.client;

import com.sun.jersey.api.client.Client;

/**
 * TODO
 *
 * @since 1.0
 */
public interface ClientFactory<T>
{

    T create( Client client, String baseUrl );

}
