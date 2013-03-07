package org.sonatype.sisu.siesta.testsuite.clients;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.sonatype.sisu.siesta.testsuite.model.UserXO;

/**
 * TODO
 *
 * @since 1.0
 */
@Path( "/user" )
public interface Users
{

    @GET
    @Path( "/inexistent/path" )
    void inexistent();

    @GET
    @Consumes( { APPLICATION_JSON } )
    List<UserXO> getJson();

    @GET
    @Path( "/{id}" )
    @Consumes( { APPLICATION_JSON } )
    UserXO getJson( @PathParam( "id" ) String id );

    @PUT
    @Produces( { APPLICATION_JSON } )
    @Consumes( { APPLICATION_JSON } )
    UserXO putJsonJson( final UserXO user );

}
