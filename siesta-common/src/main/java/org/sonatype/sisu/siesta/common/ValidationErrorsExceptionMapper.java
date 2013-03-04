package org.sonatype.sisu.siesta.common;

import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.sonatype.sisu.siesta.common.exceptions.ValidationErrorsException;
import org.sonatype.sisu.siesta.common.model.ValidationError;

/**
 * @since 1.4
 */
@Named
@Singleton
public class ValidationErrorsExceptionMapper
    extends ExceptionMapperSupport<ValidationErrorsException>
    implements ExceptionMapper<ValidationErrorsException>
{

    @Override
    protected Response convert( final ValidationErrorsException exception )
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