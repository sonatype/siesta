package org.sonatype.sisu.siesta.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ElementDescriptor;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.sonatype.sisu.siesta.common.model.ValidationError;

/**
 *
 */
@Named
@Singleton
public class ConstraintViolationExceptionMapper
    extends ExceptionMapperSupport<ConstraintViolationException>
    implements ExceptionMapper<ConstraintViolationException>
{

    @Override
    protected Response convert( final ConstraintViolationException exception )
    {
        return Response.status( getStatus( exception ) )
            .entity( new GenericEntity<List<ValidationError>>( getEntity( exception.getConstraintViolations() ) )
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

    private List<ValidationError> getEntity( final Set<ConstraintViolation<?>> violations )
    {
        final List<ValidationError> errors = new ArrayList<ValidationError>();

        for ( final ConstraintViolation violation : violations )
        {
            errors.add( new ValidationError( getPath( violation ), violation.getMessage() ) );
        }

        return errors;
    }

    private Response.Status getStatus( final ConstraintViolationException exception )
    {
        return getResponseStatus( exception.getConstraintViolations() );
    }

    private Response.Status getResponseStatus( final Set<ConstraintViolation<?>> constraintViolations )
    {
        final Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();

        if ( iterator.hasNext() )
        {
            return getResponseStatus( iterator.next() );
        }
        else
        {
            return Response.Status.BAD_REQUEST;
        }
    }

    private Response.Status getResponseStatus( final ConstraintViolation<?> constraintViolation )
    {
        for ( final Path.Node node : constraintViolation.getPropertyPath() )
        {
            final ElementDescriptor.Kind kind = node.getElementDescriptor().getKind();

            if ( ElementDescriptor.Kind.RETURN_VALUE.equals( kind ) )
            {
                return Response.Status.INTERNAL_SERVER_ERROR;
            }
        }

        return Response.Status.BAD_REQUEST;
    }

    private String getPath( final ConstraintViolation violation )
    {
        final String leafBeanName = violation.getLeafBean().getClass().getSimpleName();
        final String propertyPath = violation.getPropertyPath().toString();

        return leafBeanName + ( !"".equals( propertyPath ) ? '.' + propertyPath : "" );
    }

}