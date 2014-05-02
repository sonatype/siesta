package org.sonatype.siesta.server.internal.resteasy;

import javax.inject.Singleton;

import org.sonatype.siesta.server.ComponentContainer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.jboss.resteasy.core.Dispatcher;

/**
 * RESTEasy module.
 *
 * @since 2.0
 */
public class ResteasyModule
  extends AbstractModule
{
  @Override
  protected void configure() {
    bind(ComponentContainer.class).to(ComponentContainerImpl.class).in(Singleton.class);
  }

  /**
   * Expose RESTEasy {@link Dispatcher} binding.
   */
  @Provides
  public Dispatcher dispatcher(final ComponentContainer container) {
    return ((ComponentContainerImpl)container).getDispatcher();
  }
}
