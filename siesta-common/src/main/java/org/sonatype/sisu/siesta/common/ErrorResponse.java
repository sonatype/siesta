package org.sonatype.sisu.siesta.common;

/**
 * @since 1.4
 */
public class ErrorResponse
{

    public static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    private final int statusCode;

    private final String messageBody;

    public int getStatusCode()
    {
        return statusCode;
    }

    public String getMessageBody()
    {
        return messageBody;
    }

    ErrorResponse( int statusCode, String messageBody )
    {
        this.statusCode = statusCode;
        this.messageBody = messageBody;
    }

    @Override
    public String toString()
    {
        return statusCode + " " + messageBody;
    }

}
