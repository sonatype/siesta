/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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
package org.sonatype.siesta.testsuite;

import java.util.EnumSet;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.sonatype.sisu.litmus.testsupport.inject.InjectedTestSupport;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import org.eclipse.jetty.servlet.ServletTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Support for Siesta tests.
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
  public void configure(final Binder binder) {
    binder.install(new TestModule());
  }

  @Before
  public void startJetty() throws Exception {
    servletTester = new ServletTester();
    servletTester.getContext().addEventListener(new GuiceServletContextListener()
    {
      @Override
      protected Injector getInjector() {
        return injector;
      }
    });

    url = servletTester.createConnector(true) + TestModule.MOUNT_POINT;
    servletTester.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    servletTester.addServlet(DummyServlet.class, "/*");
    servletTester.start();

    client = ClientBuilder.newClient();
  }

  @After
  public void stopJetty() throws Exception {
    if (servletTester != null) {
      servletTester.stop();
    }
  }

  protected Client client() { return client; }

  protected String url() {
    return url;
  }

  protected String url(final String path) {
    return url + "/" + path;
  }
}
