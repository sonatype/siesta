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
package org.sonatype.sisu.siesta.server.internal.jersey;

import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.sonatype.sisu.siesta.server.ApplicationContainer;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.sun.jersey.api.core.ExtendedUriInfo;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.HttpResponseContext;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.spi.MessageBodyWorkers;
import com.sun.jersey.spi.container.ExceptionMapperContext;
import com.sun.jersey.spi.container.WebApplication;

/**
 * Siesta Jersey Guice module.
 *
 * @since 1.0
 */
public class SiestaJerseyModule
    implements Module
{
  public void configure(final Binder binder) {
    binder.bind(ApplicationContainer.class).to(JerseyContainer.class).in(Singleton.class);
    binder.bind(IoCComponentProviderFactory.class).to(SisuComponentProviderFactory.class).in(Singleton.class);
  }

  /**
   * @since 1.4
   */
  @Provides
  public WebApplication webApp(ApplicationContainer applicationContainer) {
    return ((JerseyContainer) applicationContainer).getWebApplication();
  }

  /**
   * @since 1.4
   */
  @Provides
  public Providers providers(WebApplication webApplication) {
    return webApplication.getProviders();
  }

  /**
   * @since 1.4
   */
  @Provides
  public FeaturesAndProperties featuresAndProperties(WebApplication webApplication) {
    return webApplication.getFeaturesAndProperties();
  }

  /**
   * @since 1.4
   */
  @Provides
  public MessageBodyWorkers messageBodyWorkers(WebApplication webApplication) {
    return webApplication.getMessageBodyWorkers();
  }

  /**
   * @since 1.4
   */
  @Provides
  public ExceptionMapperContext exceptionMapperContext(WebApplication webApplication) {
    return webApplication.getExceptionMapperContext();
  }

  /**
   * @since 1.4
   */
  @Provides
  public ResourceContext resourceContext(WebApplication webApplication) {
    return webApplication.getResourceContext();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public HttpContext httpContext(WebApplication webApplication) {
    return webApplication.getThreadLocalHttpContext();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public UriInfo uriInfo(WebApplication wa) {
    return wa.getThreadLocalHttpContext().getUriInfo();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public ExtendedUriInfo extendedUriInfo(WebApplication wa) {
    return wa.getThreadLocalHttpContext().getUriInfo();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public HttpRequestContext requestContext(WebApplication wa) {
    return wa.getThreadLocalHttpContext().getRequest();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public HttpHeaders httpHeaders(WebApplication wa) {
    return wa.getThreadLocalHttpContext().getRequest();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public Request request(WebApplication wa) {
    return wa.getThreadLocalHttpContext().getRequest();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public SecurityContext securityContext(WebApplication wa) {
    return wa.getThreadLocalHttpContext().getRequest();
  }

  /**
   * @since 1.4
   */
  @RequestScoped
  @Provides
  public HttpResponseContext responseContext(WebApplication wa) {
    return wa.getThreadLocalHttpContext().getResponse();
  }

}
