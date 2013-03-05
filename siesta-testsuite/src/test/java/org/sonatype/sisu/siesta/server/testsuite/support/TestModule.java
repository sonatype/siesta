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
package org.sonatype.sisu.siesta.server.testsuite.support;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.sisu.siesta.common.Resource;
import org.sonatype.sisu.siesta.jackson.SiestaJacksonModule;
import org.sonatype.sisu.siesta.server.internal.ComponentDiscoveryApplication;
import org.sonatype.sisu.siesta.server.internal.ComponentDiscoveryReporter;
import org.sonatype.sisu.siesta.server.internal.ComponentDiscoveryReporterImpl;
import org.sonatype.sisu.siesta.server.internal.SiestaModule;
import org.sonatype.sisu.siesta.server.internal.SiestaServlet;
import org.sonatype.sisu.siesta.server.internal.jersey.SiestaJerseyModule;
import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;

/**
 * @since 1.4
 */
@Named
@Singleton
public class TestModule
    extends AbstractModule
{

    public static final String MOUNT_POINT = "/siesta";

    @Override
    protected void configure()
    {
        install( new SiestaModule() );
        install( new SiestaJerseyModule() );
        install( new SiestaJacksonModule() );

        // Dynamically discover JAX-RS components
        bind( javax.ws.rs.core.Application.class ).to( ComponentDiscoveryApplication.class ).in( Singleton.class );

        // Customize the report to include the MOUNT_POINT
        bind( ComponentDiscoveryReporter.class ).toInstance( new ComponentDiscoveryReporterImpl()
        {
            @Override
            protected String pathOf( final Class<Resource> type )
            {
                String path = super.pathOf( type );
                if ( !path.startsWith( "/" ) )
                {
                    path = "/" + path;
                }
                return MOUNT_POINT + path;
            }
        } );

        install( new JerseyServletModule() );

        install( new ServletModule()
        {
            @Override
            protected void configureServlets()
            {
                serve( MOUNT_POINT + "/*" ).with( SiestaServlet.class );
            }
        } );
    }

}
