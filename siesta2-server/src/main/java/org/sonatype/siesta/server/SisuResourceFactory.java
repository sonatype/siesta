package org.sonatype.siesta.server;

import org.eclipse.sisu.BeanEntry;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.PropertyInjector;
import org.jboss.resteasy.spi.ResourceFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sisu {@link ResourceFactory}.
 *
 * @since 2.0
 */
public class SisuResourceFactory
    implements ResourceFactory
{
  private final BeanEntry<?,?> entry;

  private PropertyInjector propertyInjector;

  public SisuResourceFactory(final BeanEntry<?, ?> entry) {
    this.entry = checkNotNull(entry);
  }

  public Class<?> getScannableClass() {
    return entry.getImplementationClass();
  }

  public void registered(final ResteasyProviderFactory factory) {
    propertyInjector = factory.getInjectorFactory().createPropertyInjector(getScannableClass(), factory);
  }

  public Object createResource(final HttpRequest request,
                               final HttpResponse response,
                               final ResteasyProviderFactory factory)
  {
    final Object resource = entry.getValue();
    propertyInjector.inject(request, response, resource);
    return resource;
  }

  public void requestFinished(final HttpRequest request, final HttpResponse response, final Object resource) {
    // ignore
  }

  public void unregistered() {
    // ignore
  }
}
