package org.sonatype.sisu.siesta.client.internal;

import java.util.List;

import org.sonatype.sisu.siesta.common.validation.ValidationErrorXO;
import org.sonatype.sisu.siesta.common.validation.ValidationErrorsException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

/**
 * TODO
 *
 * @since 1.0
 */
public abstract class ValidationErrorsV1FilterSupport
    extends ErrorResponseFilterSupport
{

    protected void throwException( final ClientResponse response )
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
