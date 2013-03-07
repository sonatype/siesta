package org.sonatype.sisu.siesta.client.internal;

import static javax.ws.rs.core.MediaType.WILDCARD_TYPE;

import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 * TODO
 *
 * @since 1.4
 */
public abstract class ErrorResponseFilterSupport
    extends ClientFilter
{

    @Override
    public ClientResponse handle( final ClientRequest request )
        throws ClientHandlerException
    {
        final List<Object> accept = request.getHeaders().get( "Accept" );
        final MediaType mediaType = getMediaType();
        if ( !acceptsMediaType( accept, WILDCARD_TYPE ) && !acceptsMediaType( accept, mediaType ) )
        {
            request.getHeaders().add( "Accept", mediaType );
        }
        final ClientResponse response = getNext().handle( request );
        if ( !Response.Status.Family.SUCCESSFUL.equals( response.getClientResponseStatus().getFamily() )
            && mediaType.equals( response.getType() ) )
        {
            throwException( response );
        }
        return response;
    }

    protected abstract MediaType getMediaType();

    protected abstract void throwException( final ClientResponse response );

    private boolean acceptsMediaType( final List<Object> accept, final MediaType mediaType )
    {
        return accept != null && ( accept.contains( mediaType ) || accept.contains( mediaType.toString() ) );
    }

}
