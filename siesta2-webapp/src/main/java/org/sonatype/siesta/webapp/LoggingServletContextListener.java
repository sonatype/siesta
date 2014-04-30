package org.sonatype.siesta.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Logging {@link ServletContextListener}.
 *
 * @since 2.0
 */
public class LoggingServletContextListener
    implements ServletContextListener
{
  @Override
  public void contextInitialized(final ServletContextEvent event) {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    LoggerFactory.getLogger(getClass()).debug("JUL bridge initialized");
  }

  @Override
  public void contextDestroyed(final ServletContextEvent event) {
    SLF4JBridgeHandler.uninstall();
  }
}
