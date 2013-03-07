package org.sonatype.sisu.siesta.client.internal;

import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_ERROR_V1_JSON_TYPE;
import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_ERROR_V1_XML_TYPE;
import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_VALIDATION_ERRORS_V1_JSON_TYPE;
import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_VALIDATION_ERRORS_V1_XML_TYPE;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sonatype.sisu.siesta.common.SiestaMediaType;
import org.sonatype.sisu.siesta.common.error.ErrorXO;
import org.sonatype.sisu.siesta.common.validation.ValidationErrorXO;
import org.sonatype.sisu.siesta.common.validation.ValidationErrorsException;
import com.google.common.base.Throwables;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * TODO
 *
 * @since 1.0
 */
public class ClientImpl
    implements InvocationHandler
{

    private final Class<?> serviceInterface;

    private final Client client;

    private final String baseUrl;

    public ClientImpl( final Class<?> serviceInterface, final Client client, final String baseUrl )
    {
        this.serviceInterface = serviceInterface;
        this.client = client;
        try
        {
            if ( baseUrl.endsWith( "/" ) )
            {
                this.baseUrl = baseUrl.substring( 0, baseUrl.length() - 1 );
            }
            else
            {
                this.baseUrl = baseUrl;
            }
        }
        catch ( Exception e )
        {
            throw Throwables.propagate( e );
        }
    }

    public Object invoke( final Object proxy, final Method method, final Object[] args )
        throws Throwable
    {
        final String url = getUrl( method, args );
        final String httpMethod = getHttpMethod( method );
        final String type = getType( method );
        final String[] accepts = getAccepts( method );
        final Object payload = getPayload( method, args );

        final WebResource.Builder builder = client.resource( url ).accept( accepts );

        if ( type != null )
        {
            builder.type( type );
        }

        ClientResponse response = null;
        if ( payload == null )
        {
            response = builder.method( httpMethod, ClientResponse.class );
        }
        else
        {
            response = builder.method( httpMethod, ClientResponse.class, payload );
        }

        if ( Response.Status.Family.SUCCESSFUL.equals( response.getClientResponseStatus().getFamily() ) )
        {
            final Class<?> returnType = method.getReturnType();
            if ( !Void.class.equals( returnType ) )
            {
                return response.getEntity( returnType );
            }
            else
            {
                return null;
            }
        }

        handleErrorIfPresent( response );
        handleValidationErrorIfPresent( response );

        throw new UniformInterfaceException( response );
    }

    private void handleValidationErrorIfPresent( final ClientResponse response )
    {
        if ( VND_VALIDATION_ERRORS_V1_JSON_TYPE.equals( response.getType() )
            || VND_VALIDATION_ERRORS_V1_XML_TYPE.equals( response.getType() ) )
        {
            List<ValidationErrorXO> validationErrors = null;
            try
            {
                validationErrors = response.getEntity(
                    new GenericType<List<ValidationErrorXO>>()
                    {
                    }
                );
            }
            catch ( Exception e )
            {
                // ignore
            }
            if ( validationErrors != null )
            {
                throw new ValidationErrorsException().withErrors( validationErrors );
            }
        }
    }

    private void handleErrorIfPresent( final ClientResponse response )
    {
        if ( VND_ERROR_V1_JSON_TYPE.equals( response.getType() )
            || VND_ERROR_V1_XML_TYPE.equals( response.getType() ) )
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

    private Object getPayload( final Method method, final Object[] args )
    {
        if ( args != null )
        {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for ( int i = 0; i < args.length; i++ )
            {
                final Annotation[] annotations = parameterAnnotations[i];
                if ( annotations.length > 0 )
                {
                    for ( final Annotation annotation : annotations )
                    {
                        if ( !( annotation instanceof PathParam ) && !( annotation instanceof QueryParam ) )
                        {
                            return args[i];
                        }
                    }
                }
                else
                {
                    return args[i];
                }
            }
        }
        return null;
    }

    private String[] getAccepts( final Method method )
    {
        final Consumes consumes = method.getAnnotation( Consumes.class );
        if ( consumes != null )
        {
            final String[] values = consumes.value();
            final String[] accepts = new String[values.length + 2];

            System.arraycopy( values, 0, accepts, 0, values.length );

            accepts[values.length] = SiestaMediaType.VND_ERROR_V1_JSON;
            accepts[values.length + 1] = SiestaMediaType.VND_VALIDATION_ERRORS_V1_JSON;

            return accepts;
        }
        return new String[]{ "*/*" };
    }

    private String getType( final Method method )
    {
        final Produces produces = method.getAnnotation( Produces.class );
        if ( produces != null )
        {
            return produces.value()[0];
        }
        return null;
    }

    private String getHttpMethod( final Method method )
    {
        final Annotation[] annotations = method.getAnnotations();
        if ( annotations != null )
        {
            for ( final Annotation annotation : annotations )
            {
                final HttpMethod httpMethod = annotation.annotationType().getAnnotation( HttpMethod.class );
                if ( httpMethod != null )
                {
                    return httpMethod.value();
                }
            }
        }
        throw new IllegalStateException( "Method " + method + " is not annotated with any of @GET/..." );
    }

    private String getUrl( final Method method, final Object[] args )
    {
        final StringBuilder rawUrl = new StringBuilder();
        final Path atClass = serviceInterface.getAnnotation( Path.class );
        if ( atClass != null )
        {
            rawUrl.append( atClass.value() );
        }
        else
        {
            final Path atDeclaringClass = method.getDeclaringClass().getAnnotation( Path.class );
            if ( atDeclaringClass != null )
            {
                rawUrl.append( atDeclaringClass.value() );
            }
        }
        final Path atMethod = method.getAnnotation( Path.class );
        if ( atMethod != null )
        {
            rawUrl.append( atMethod.value() );
        }
        if ( rawUrl.toString().trim().length() == 0 )
        {
            throw new IllegalStateException( String.format(
                "Cannot calculate rest URL for [%s]. Is class and/or method annotated with @Path?",
                method.getName() )
            );
        }

        String url = rawUrl.toString();
        if ( url.contains( "{" ) && url.contains( "}" ) )
        {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for ( int i = 0; i < parameterAnnotations.length; i++ )
            {
                if ( args.length >= i )
                {
                    final Annotation[] annotations = parameterAnnotations[i];
                    for ( final Annotation annotation : annotations )
                    {
                        if ( annotation instanceof PathParam )
                        {
                            final String name = ( (PathParam) annotation ).value();
                            if ( args[i] != null )
                            {
                                url = url.replace( "{" + name + "}", args[i].toString() );
                            }
                        }
                    }
                }
            }
        }
        return baseUrl + url;
    }

}
