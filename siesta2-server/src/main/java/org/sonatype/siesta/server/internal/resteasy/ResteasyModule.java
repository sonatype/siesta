package org.sonatype.siesta.server.internal.resteasy;

import javax.inject.Singleton;

import org.sonatype.siesta.server.ComponentContainer;

import com.google.inject.AbstractModule;

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
}
