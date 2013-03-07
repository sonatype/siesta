package org.sonatype.sisu.siesta.client.internal;

import org.sonatype.sisu.siesta.common.error.ErrorXO;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * TODO
 *
 * @since 1.0
 */
public abstract class ErrorsV1FilterSupport
    extends ErrorResponseFilterSupport
{

    protected void throwException( final ClientResponse response )
    {
        ErrorXO error = null;
        try
        {
            error = response.getEntity( ErrorXO.class );
        }
        catch ( Exception e )
        {
            // ignore
        }
        if ( error != null )
        {
            throw new UniformInterfaceException( error.getMessage() + " (" + error.getId() + ")", response );
        }
    }

}
