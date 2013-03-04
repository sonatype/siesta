package org.sonatype.sisu.siesta.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.core.Response;

/**
 * Indicates the HTTP status code that should be used to report the annotated exception. Any exception that misses this
 * annotation will be treated as an internal server error and reported as HTTP 500.
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@Inherited
public @interface HttpStatusCode
{

    int value();

}
