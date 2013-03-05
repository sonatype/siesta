/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.sisu.siesta.server.testsuite;

import static com.sun.jersey.api.client.ClientResponse.Status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.sonatype.sisu.siesta.common.exceptions.ErrorXO;
import org.sonatype.sisu.siesta.server.testsuite.support.SiestaTestSupport;
import com.sun.jersey.api.client.ClientResponse;

/**
 * @since 1.4
 */
public class ThrowsTest
    extends SiestaTestSupport
{

    @Test
    public void throwObjectNotFoundException_XML()
        throws Exception
    {
        throwException( MediaType.APPLICATION_XML_TYPE, "ObjectNotFoundException", Status.NOT_FOUND );
    }

    @Test
    public void throwObjectNotFoundException_JSON()
        throws Exception
    {
        throwException( MediaType.APPLICATION_JSON_TYPE, "ObjectNotFoundException", Status.NOT_FOUND );
    }

    @Test
    public void throwBadRequestException_XML()
        throws Exception
    {
        throwException( MediaType.APPLICATION_XML_TYPE, "BadRequestException", Status.BAD_REQUEST );
    }

    @Test
    public void throwBadRequestException_JSON()
        throws Exception
    {
        throwException( MediaType.APPLICATION_JSON_TYPE, "BadRequestException", Status.BAD_REQUEST );
    }

    public void throwException( final MediaType mediaType, final String exceptionType, final Status expectedStatus )
        throws Exception
    {
        final ClientResponse response = client().resource( url( "throw/" + exceptionType ) )
            .type( mediaType )
            .accept( mediaType )
            .get( ClientResponse.class );

        assertThat( response.getClientResponseStatus(), equalTo( expectedStatus ) );

        final ErrorXO error = response.getEntity( ErrorXO.class );
        assertThat( error, is( notNullValue() ) );
        assertThat( error.getUuid(), is( notNullValue() ) );
        assertThat( error.getMessage(), is( exceptionType ) );
    }

}
