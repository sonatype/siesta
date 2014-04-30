package org.sonatype.siesta.server;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Siesta servlet.
 *
 * @since 2.0
 */
@Named
@Singleton
public class SiestaServlet
  extends ServletContainer
{
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final ResourceConfigReporter reporter;

  @Inject
  public SiestaServlet(final ResourceConfig config,
                       final @Nullable ResourceConfigReporter reporter)
  {
    super(checkNotNull(config));

    this.reporter = reporter;
    log.debug("Reporter: {}", reporter);
  }

  @Override
  protected void init(final WebConfig webConfig) throws ServletException {
    super.init(webConfig);

    if (reporter != null) {
      reporter.report(getConfiguration());
    }
  }

  @Override
  public void reload(final ResourceConfig configuration) {
    super.reload(configuration);

    if (reporter != null) {
      reporter.report(configuration);
    }
  }

  @Override
  public void service(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException
  {
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
      super.service(request, response);
    }
    finally {
      MDC.remove(getClass().getName());
    }
  }
}
