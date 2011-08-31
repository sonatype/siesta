/*
 * Copyright (c) 2007-2011 Sonatype, Inc. All rights reserved.
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

import org.sonatype.sisu.siesta.server.ApplicationContainer;
import org.sonatype.sisu.siesta.server.ApplicationLocator;
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

    private final ApplicationLocator locator;

    @Inject
    public SiestaServlet(final ApplicationContainer container, final ApplicationLocator locator) {
        this.container = checkNotNull(container);
        log.debug("Container: {}", container);
        this.locator = checkNotNull(locator);
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
        checkNotNull(config);
        log.debug("Initializing");
        super.init(config);
        container.init(config);
        for (Application application : locator.locate()) {
            container.add(application);
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
            log.debug("Processing: {} {} ({})", new Object[] {request.getMethod(), uri, request.getRequestURL()});
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