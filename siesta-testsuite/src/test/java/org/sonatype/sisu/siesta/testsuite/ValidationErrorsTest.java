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

import static com.sun.jersey.api.client.ClientResponse.Status;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_VALIDATION_ERRORS_V1_JSON_TYPE;
import static org.sonatype.sisu.siesta.common.SiestaMediaType.VND_VALIDATION_ERRORS_V1_XML_TYPE;

import java.util.List;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.sonatype.sisu.siesta.common.validation.ValidationErrorXO;
import org.sonatype.sisu.siesta.testsuite.model.UserXO;
import org.sonatype.sisu.siesta.testsuite.support.SiestaTestSupport;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

/**
 * @since 1.4
 */
public class ValidationErrorsTest
    extends SiestaTestSupport
{

    @Test
    public void put_multiple_manual_validations_XML()
        throws Exception
    {
        put_multiple_manual_validations( APPLICATION_XML_TYPE, VND_VALIDATION_ERRORS_V1_XML_TYPE );
    }

    @Test
    public void put_multiple_manual_validations_JSON()
        throws Exception
    {
        put_multiple_manual_validations( APPLICATION_JSON_TYPE, VND_VALIDATION_ERRORS_V1_JSON_TYPE );
    }

    public void put_multiple_manual_validations( final MediaType... mediaTypes )
        throws Exception
    {
        final UserXO sent = new UserXO();

        final ClientResponse response = client().resource( url( "validationErrors/manual/multiple" ) )
            .type( mediaTypes[0] )
            .accept( mediaTypes )
            .put( ClientResponse.class, sent );

        assertThat( response.getClientResponseStatus(), is( equalTo( Status.BAD_REQUEST ) ) );
        assertThat( response.getType(), is( equalTo( mediaTypes[1] ) ) );

        final List<ValidationErrorXO> errors = response.getEntity( new GenericType<List<ValidationErrorXO>>()
        {
        } );
        assertThat( errors, hasSize( 2 ) );
    }

    @Test
    public void put_single_manual_validation_XML()
        throws Exception
    {
        put_single_manual_validation( APPLICATION_XML_TYPE, VND_VALIDATION_ERRORS_V1_XML_TYPE );
    }

    @Test
    public void put_single_manual_validation_JSON()
        throws Exception
    {
        put_single_manual_validation( APPLICATION_JSON_TYPE, VND_VALIDATION_ERRORS_V1_JSON_TYPE );
    }

    public void put_single_manual_validation( final MediaType... mediaTypes )
        throws Exception
    {
        final UserXO sent = new UserXO();

        final ClientResponse response = client().resource( url( "validationErrors/manual/single" ) )
            .type( mediaTypes[0] )
            .accept( mediaTypes )
            .put( ClientResponse.class, sent );

        assertThat( response.getClientResponseStatus(), is( equalTo( Status.BAD_REQUEST ) ) );
        assertThat( response.getType(), is( equalTo( mediaTypes[1] ) ) );

        final List<ValidationErrorXO> errors = response.getEntity( new GenericType<List<ValidationErrorXO>>()
        {
        } );
        assertThat( errors, hasSize( 1 ) );
    }

}
