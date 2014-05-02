package org.sonatype.siesta.server.internal.resteasy;

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

  @Override
  public Class<?> getScannableClass() {
    return entry.getImplementationClass();
  }

  @Override
  public void registered(final ResteasyProviderFactory factory) {
    propertyInjector = factory.getInjectorFactory().createPropertyInjector(getScannableClass(), factory);
  }

  @Override
  public Object createResource(final HttpRequest request,
                               final HttpResponse response,
                               final ResteasyProviderFactory factory)
  {
    final Object resource = entry.getValue();
    propertyInjector.inject(request, response, resource);
    return resource;
  }

  @Override
  public void requestFinished(final HttpRequest request, final HttpResponse response, final Object resource) {
    // ignore
  }

  @Override
  public void unregistered() {
    // ignore
  }
}
