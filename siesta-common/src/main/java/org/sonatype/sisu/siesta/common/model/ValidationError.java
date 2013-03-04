package org.sonatype.sisu.siesta.common.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @since 1.4
 */
@XmlRootElement(name = "validationError")
public class ValidationError
{

    private String id;

    private String message;

    public ValidationError()
    {
        id = "*";
    }

    public ValidationError( final String message )
    {
        this( "*", message );
    }

    public ValidationError( final String id, final String message )
    {
        this.id = checkNotNull( id );
        this.message = checkNotNull( message );
    }

    public String getId()
    {
        return id;
    }

    public void setId( final String id )
    {
        this.id = id;
    }

    public ValidationError withId( final String id )
    {
        this.id = id;
        return this;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( final String message )
    {
        this.message = message;
    }

    public ValidationError withMessage( final String message )
    {
        this.message = message;
        return this;
    }

    @Override
    public String toString()
    {
        return id + " -> " + message;
    }

}