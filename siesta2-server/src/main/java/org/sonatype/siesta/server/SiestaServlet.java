package org.sonatype.siesta.server;

import javax.inject.Named;
import javax.inject.Singleton;

import org.glassfish.jersey.servlet.ServletContainer;

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

}
