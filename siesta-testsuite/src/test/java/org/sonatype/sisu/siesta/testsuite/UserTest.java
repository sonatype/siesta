/*
 * Copyright (c) 2007-2013 Sonatype, Inc. All rights reserved.
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
package org.sonatype.sisu.siesta.testsuite;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
import static javax.ws.rs.core.Response.Status.Family;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;
import java.util.UUID;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.sonatype.sisu.siesta.testsuite.model.UserXO;
import org.sonatype.sisu.siesta.testsuite.support.SiestaTestSupport;
import com.sun.jersey.api.client.ClientResponse;

/**
 * Tests related to happy paths for a resource.
 *
 * @since 1.4
 */
public class UserTest
    extends SiestaTestSupport
{

    @Test
    public void put_happyPath_XML()
        throws Exception
    {
        put_happyPath( APPLICATION_XML_TYPE );
    }

    @Test
    public void put_happyPath_JSON()
        throws Exception
    {
        put_happyPath( APPLICATION_JSON_TYPE );
    }

    public void put_happyPath( final MediaType mediaType )
        throws Exception
    {
        final UserXO sent = new UserXO().withName( UUID.randomUUID().toString() ).withCreated( new Date() );

        final ClientResponse response = client().resource( url( "user" ) )
            .type( mediaType )
            .accept( mediaType )
            .put( ClientResponse.class, sent );

        assertThat( response.getClientResponseStatus().getFamily(), equalTo( Family.SUCCESSFUL ) );

        final UserXO received = response.getEntity( UserXO.class );

        assertThat( received, is( notNullValue() ) );
        assertThat( received.getName(), is( equalTo( sent.getName() ) ) );
        assertThat( received.getCreated(), is( equalTo( sent.getCreated() ) ) );
    }

}
