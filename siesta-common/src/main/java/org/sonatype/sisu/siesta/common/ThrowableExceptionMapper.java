package org.sonatype.sisu.siesta.common;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.google.common.base.Preconditions;

/**
 * Ensures that any exceptions internally created by the JAX-RS container have a plain text body with the error message.
 *
 * @since 1.4
 */
@Named
@Singleton
public class ThrowableExceptionMapper
    extends ExceptionMapperSupport<Throwable>
    implements ExceptionMapper<Throwable>
{

    private ErrorResponseGenerator errorResponseGenerator;

    @Inject
    public ThrowableExceptionMapper( final ErrorResponseGenerator errorResponseGenerator )
    {
        this.errorResponseGenerator = checkNotNull( errorResponseGenerator );
    }

    protected Response convert( Throwable e )
    {
        final ErrorResponse response = errorResponseGenerator.mapException( e );

        return Response.status( response.getStatusCode() )
            .type( ErrorResponse.CONTENT_TYPE )
            .entity( response.getMessageBody() )
            .build();
    }

}
