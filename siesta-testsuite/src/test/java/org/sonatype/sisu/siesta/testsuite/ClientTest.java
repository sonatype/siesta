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
package org.sonatype.sisu.siesta.testsuite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.sonatype.sisu.siesta.client.ClientBuilder.Target.Factory;

import java.util.Date;
import java.util.UUID;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.sonatype.sisu.siesta.client.ClientBuilder;
import org.sonatype.sisu.siesta.testsuite.clients.Errors;
import org.sonatype.sisu.siesta.testsuite.clients.Users;
import org.sonatype.sisu.siesta.testsuite.model.UserXO;
import org.sonatype.sisu.siesta.testsuite.support.SiestaClientTestSupport;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * @since 1.4
 */
public class ClientTest
    extends SiestaClientTestSupport
{

    private Users users;

    private Errors errors;

    @Before
    public void createClient()
    {
        final Factory factory = ClientBuilder.using( client() ).toAccess( url() );

        users = factory.build( Users.class );
        errors = factory.build( Errors.class );
    }

    @Test
    public void getJson()
        throws Exception
    {
        users.getJson();
    }

    @Test
    public void getXML()
        throws Exception
    {
        users.getXml();
    }

    @Test
    public void getByIdJson()
        throws Exception
    {
        users.getJson( "foo" );
    }

    @Test
    public void getByIdXml()
        throws Exception
    {
        users.getXml( "foo" );
    }

    @Test
    public void getByIdInexistentJson()
        throws Exception
    {
        thrown.expect( UniformInterfaceException.class );
        thrown.expectMessage( "User with id 'test' not found" );

        users.getJson( "test" );
    }

    @Test
    public void getByIdInexistentXml()
        throws Exception
    {
        thrown.expect( UniformInterfaceException.class );
        thrown.expectMessage( "User with id 'test' not found" );

        users.getXml( "test" );
    }

    @Test
    public void inexistentPath()
        throws Exception
    {
        thrown.expect( UniformInterfaceException.class );
        thrown.expect( new HttpStatusMatcher( 404 ) );

        users.inexistent();
    }

    @Test
    public void putJsonJson()
        throws Exception
    {
        final UserXO sent = new UserXO().withName( UUID.randomUUID().toString() ).withCreated( new Date() );
        final UserXO received = users.putJsonJson( sent );

        assertThat( received, is( notNullValue() ) );
        assertThat( received.getName(), is( equalTo( sent.getName() ) ) );
        assertThat( received.getCreated(), is( equalTo( sent.getCreated() ) ) );
    }

    @Test
    public void putXmlXml()
        throws Exception
    {
        final UserXO sent = new UserXO().withName( UUID.randomUUID().toString() ).withCreated( new Date() );
        final UserXO received = users.putXmlXml( sent );

        assertThat( received, is( notNullValue() ) );
        assertThat( received.getName(), is( equalTo( sent.getName() ) ) );
        assertThat( received.getCreated(), is( equalTo( sent.getCreated() ) ) );
    }

    @Test
    public void putXmlJson()
        throws Exception
    {
        final UserXO sent = new UserXO().withName( UUID.randomUUID().toString() ).withCreated( new Date() );
        final UserXO received = users.putXmlJson( sent );

        assertThat( received, is( notNullValue() ) );
        assertThat( received.getName(), is( equalTo( sent.getName() ) ) );
        assertThat( received.getCreated(), is( equalTo( sent.getCreated() ) ) );
    }

    @Test
    public void putJsonXml()
        throws Exception
    {
        final UserXO sent = new UserXO().withName( UUID.randomUUID().toString() ).withCreated( new Date() );
        final UserXO received = users.putJsonXml( sent );

        assertThat( received, is( notNullValue() ) );
        assertThat( received.getName(), is( equalTo( sent.getName() ) ) );
        assertThat( received.getCreated(), is( equalTo( sent.getCreated() ) ) );
    }

    @Test
    public void put()
        throws Exception
    {
        final UserXO sent = new UserXO().withName( UUID.randomUUID().toString() ).withCreated( new Date() );
        final UserXO received = users.put( sent );

        assertThat( received, is( notNullValue() ) );
        assertThat( received.getName(), is( equalTo( sent.getName() ) ) );
        assertThat( received.getCreated(), is( equalTo( sent.getCreated() ) ) );
    }

    @Test
    public void throwObjectNotFoundException()
        throws Exception
    {
        thrown.expect( UniformInterfaceException.class );
        thrown.expect( new HttpStatusMatcher( 404 ) );
        thrown.expectMessage( "ObjectNotFoundException" );

        errors.throwObjectNotFoundException();
    }

    @Test
    public void throwBadRequestException()
        throws Exception
    {
        thrown.expect( UniformInterfaceException.class );
        thrown.expect( new HttpStatusMatcher( 400 ) );
        thrown.expectMessage( "BadRequestException" );

        errors.throwBadRequestException();
    }

    private static class HttpStatusMatcher
        extends TypeSafeMatcher<UniformInterfaceException>
    {

        private final int expectedStatus;

        HttpStatusMatcher( final int expectedStatus )
        {
            this.expectedStatus = expectedStatus;
        }

        @Override
        protected boolean matchesSafely( final UniformInterfaceException e )
        {
            return e.getResponse().getStatus() == expectedStatus;
        }

        public void describeTo( final Description description )
        {
            description.appendText( "HTTP Status " + expectedStatus );
        }
    }

}
