package org.sonatype.sisu.siesta.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates an {@link ErrorResponse} based on information available in exception.
 *
 * @since 1.4
 */
@Named
public class ErrorResponseGenerator
{

    private static final Logger log = LoggerFactory.getLogger( ErrorResponseGenerator.class );

    private boolean propagateErrorDetails;

    @Inject
    public ErrorResponseGenerator( @Named( "${propagate-error-details:-false}" ) boolean propagateErrorDetails )
    {
        this.propagateErrorDetails = propagateErrorDetails;
    }

    public ErrorResponse mapException( final Throwable e )
    {
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        if ( e instanceof WebApplicationException )
        {
            Response resp = ( (WebApplicationException) e ).getResponse();
            statusCode = resp.getStatus();
        }
        else
        {
            HttpStatusCode hsc = e.getClass().getAnnotation( HttpStatusCode.class );
            if ( hsc != null )
            {
                statusCode = hsc.value();
            }
        }

        return mapException( e, statusCode );
    }

    public ErrorResponse mapException( final Throwable e, final int statusCode )
    {
        String id = UUID.randomUUID().toString().replace( "-", "" ).substring( 0, 16 );

        if ( log.isDebugEnabled() )
        {
            log.error( "{}/{} -> {} (ID {})", e.getClass().getName(), e.getMessage(), statusCode, id, e );
        }
        else
        {
            log.error( "{}/{} -> {} (ID {})", e.getClass().getName(), e.getMessage(), statusCode, id );
        }

        StringWriter writer = new StringWriter( 256 );

        if ( statusCode == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() )
        {
            // don't reveal any specifics but use a generic message
            writer.write( Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase() );
            writer.write( " (ID " );
            writer.write( id );
            writer.write( ")" );
        }
        else if ( statusCode == Response.Status.NOT_FOUND.getStatusCode() && e instanceof WebApplicationException )
        {
            // Jersey produces crappy messages for that one
            writer.write( "Resource not found, please check your request URL." );
        }
        else if ( statusCode == Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode()
            && e instanceof WebApplicationException )
        {
            // Jersey produces crappy messages for that one
            writer.write( "Unsupported media type" );
            writer.write( ", please check your request URL, the supplied data and its content type." );
        }
        else
        {
            String msg = e.getMessage();
            if ( msg == null || msg.length() <= 0 )
            {
                Response.Status status = Response.Status.fromStatusCode( statusCode );
                if ( status != null )
                {
                    msg = status.getReasonPhrase();
                }
                else
                {
                    msg = "Error: " + statusCode;
                }
            }
            writer.write( msg );
        }

        if ( propagateErrorDetails )
        {
            writer.write( "\n\n" );
            e.printStackTrace( new PrintWriter( writer ) );
        }

        String messageBody = writer.toString().replace( "\r\n", "\n" );

        return new ErrorResponse( statusCode, messageBody );
    }

}
