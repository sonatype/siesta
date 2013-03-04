package org.sonatype.sisu.siesta.common;

import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.sonatype.sisu.siesta.common.exceptions.ValidationException;
import org.sonatype.sisu.siesta.common.model.ValidationError;

/**
 * @since 1.4
 */
@Named
@Singleton
public class ValidationExceptionMapper
    extends ExceptionMapperSupport<ValidationException>
    implements ExceptionMapper<ValidationException>
{

    @Override
    protected Response convert( final ValidationException exception )
    {
        return Response.status( Response.Status.BAD_REQUEST )
            .entity( new GenericEntity<List<ValidationError>>( exception.getErrors() )
            {
                @Override
                public String toString()
                {
                    return getEntity().toString();
                }
            }
            )
            .build();
    }

}