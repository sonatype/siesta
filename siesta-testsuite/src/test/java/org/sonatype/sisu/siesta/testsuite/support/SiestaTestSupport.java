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
package org.sonatype.sisu.siesta.testsuite.support;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.testing.ServletTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.sonatype.inject.BeanScanning;
import org.sonatype.sisu.litmus.testsupport.inject.InjectedTestSupport;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Support class for Siesta UTs.
 *
 * @since 1.4
 */
public class SiestaTestSupport
    extends InjectedTestSupport
{

    @Inject
    private Injector injector;

    private ServletTester servletTester;

    private String url;

    private Client client;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    public BeanScanning scanning()
    {
        return BeanScanning.ON;
    }

    @Before
    public void startJetty()
        throws Exception
    {
        servletTester = new ServletTester();
        servletTester.addEventListener( new GuiceServletContextListener()
        {

            @Override
            protected Injector getInjector()
            {
                return injector;
            }
        } );
        url = servletTester.createSocketConnector( true ) + TestModule.MOUNT_POINT;
        servletTester.addFilter( GuiceFilter.class, "/*", 1 );
        servletTester.addServlet( DummyServlet.class, "/*" );
        servletTester.start();

        final ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put( JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE );
        client = Client.create( clientConfig );

        client.addFilter( new LoggingFilter() );
    }

    @After
    public void stopJetty()
        throws Exception
    {
        if ( servletTester != null )
        {
            servletTester.stop();
        }
    }

    protected Client client()
    {
        return client;
    }

    protected String url()
    {
        return url;
    }

    protected String url( final String path )
    {
        return url + "/" + path;
    }

    private static class DummyServlet
        extends HttpServlet
    {

    }

}
