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
package org.sonatype.sisu.siesta.server.internal;

import com.google.inject.Key;
import org.sonatype.guice.bean.locators.BeanLocator;
import org.sonatype.inject.BeanEntry;
import org.sonatype.inject.Mediator;
import org.sonatype.sisu.siesta.server.ApplicationContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.lang.annotation.Annotation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Siesta servlet.
 *
 * @since 1.0
 */
public class SiestaServlet
    extends HttpServlet
{
    private static final Logger log = LoggerFactory.getLogger(SiestaServlet.class);

    private static final long serialVersionUID = 1L;

    private final ApplicationContainer container;

    private final BeanLocator beanLocator;

    @Inject
    public SiestaServlet(final ApplicationContainer container, final BeanLocator beanLocator) {
        this.container = checkNotNull(container);
        log.debug("Container: {}", container);
        this.beanLocator = beanLocator;
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
        checkNotNull(config);
        log.debug("Initializing");

        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        try {
            super.init(config);
            container.init(config);
            beanLocator.watch(Key.get(Application.class), new ApplicationMediator(), container);
        }
        finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    private class ApplicationMediator
        implements Mediator<Annotation,Application,ApplicationContainer>
    {
        public void add(final BeanEntry<Annotation, Application> entry, final ApplicationContainer container) throws Exception {
            log.debug("Adding application: {}", entry);
            try {
                container.add(entry.getValue());
            }
            catch (Exception e) {
                log.error("Failed to add application", e);
            }
        }

        public void remove(final BeanEntry<Annotation, Application> entry, final ApplicationContainer container) throws Exception {
            // unsupported
        }
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        checkNotNull(request);
        checkNotNull(response);

        // Log the request URI+URL muck
        String uri = request.getRequestURI();
        if (request.getQueryString() != null) {
            uri = String.format("%s?%s", uri, request.getQueryString());
        }

        if (log.isDebugEnabled()) {
            log.debug("Processing: {} {} ({})", request.getMethod(), uri, request.getRequestURL());
        }

        MDC.put(getClass().getName(), uri);
        try {
            container.service(request, response);
        }
        finally {
            MDC.remove(getClass().getName());
        }
    }
}